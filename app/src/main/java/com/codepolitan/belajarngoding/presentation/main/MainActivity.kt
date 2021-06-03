package com.codepolitan.belajarngoding.presentation.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import com.bumptech.glide.Glide
import com.codepolitan.belajarngoding.R
import com.codepolitan.belajarngoding.adapter.MaterialsAdapter
import com.codepolitan.belajarngoding.databinding.ActivityMainBinding
import com.codepolitan.belajarngoding.model.Material
import com.codepolitan.belajarngoding.model.User
import com.codepolitan.belajarngoding.presentation.content.ContentActivity
import com.codepolitan.belajarngoding.presentation.user.UserActivity
import com.codepolitan.belajarngoding.repository.Repository
import com.codepolitan.belajarngoding.utils.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {

  companion object{
    const val EXTRA_POSITION = "extra_position"
  }
  private lateinit var mainBinding: ActivityMainBinding
  private lateinit var materialsAdapter: MaterialsAdapter
  private lateinit var userDatabase: DatabaseReference
  private lateinit var materialDatabase: DatabaseReference
  private var currentUser: FirebaseUser? = null

  private var listenerUser = object : ValueEventListener {
    override fun onDataChange(snapshot: DataSnapshot) {
      hideLoading()
      val user = snapshot.getValue(User::class.java)
      user?.let {
        mainBinding.apply {
          tvNameUserMain.text = it.nameUser

          Glide
            .with(this@MainActivity)
            .load(it.avatarUser)
            .placeholder(android.R.color.darker_gray)
            .into(ivAvatarMain)
        }
      }
    }

    override fun onCancelled(error: DatabaseError) {
      hideLoading()
      Log.e("MainActivity", "[onCancelled] - ${error.message}")
      showDialogError(this@MainActivity, error.message)
    }
  }

  private var listenerMaterials = object : ValueEventListener{
    override fun onDataChange(snapshot: DataSnapshot) {
      hideLoading()
      if (snapshot.value != null){
        showData()
        val json = Gson().toJson(snapshot.value)
        val type = object : TypeToken<MutableList<Material>>() {}.type
        val materials = Gson().fromJson<MutableList<Material>>(json, type)

        materials?.let { materialsAdapter.materials = it }
      }else{
        showEmptyData()
      }
    }

    override fun onCancelled(error: DatabaseError) {
      hideLoading()
      Log.e("MainActivity", "[onCancelled] - ${error.message}")
      showDialogError(this@MainActivity, error.message)
    }
  }

  private fun showEmptyData() {
    mainBinding.apply {
      ivEmptyData.visible()
      etSearchMain.disabled()
      rvMaterialsMain.gone()
    }
  }

  private fun showData() {
    mainBinding.apply {
      ivEmptyData.gone()
      etSearchMain.enabled()
      rvMaterialsMain.visible()
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    mainBinding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(mainBinding.root)

    //Init
    materialsAdapter = MaterialsAdapter()
    userDatabase = FirebaseDatabase.getInstance().getReference("users")
    materialDatabase = FirebaseDatabase.getInstance().getReference("materials")
    currentUser = FirebaseAuth.getInstance().currentUser

    getDataFirebase()
    onAction()
  }

  private fun getDataFirebase() {
    showLoading()
    userDatabase
      .child(currentUser?.uid.toString())
      .addValueEventListener(listenerUser)

    materialDatabase
      .addValueEventListener(listenerMaterials)

    mainBinding.rvMaterialsMain.adapter = materialsAdapter
  }

  override fun onResume() {
    super.onResume()
    if (intent != null){
      val position = intent.getIntExtra(EXTRA_POSITION, 0)
      mainBinding.rvMaterialsMain.smoothScrollToPosition(position)
    }
  }

  private fun showLoading() {
    mainBinding.swipeMain.isRefreshing = true
  }

  private fun hideLoading() {
    mainBinding.swipeMain.isRefreshing = false
  }

  private fun onAction() {
    mainBinding.apply {
      ivAvatarMain.setOnClickListener {
        startActivity<UserActivity>()
      }

      etSearchMain.addTextChangedListener {
        materialsAdapter.filter.filter(it.toString())
      }

      etSearchMain.setOnEditorActionListener{_, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_SEARCH){
          val dataSearch = etSearchMain.text.toString().trim()
          materialsAdapter.filter.filter(dataSearch)
          return@setOnEditorActionListener true
        }
        return@setOnEditorActionListener false
      }

      swipeMain.setOnRefreshListener {
        getDataFirebase()
      }
    }

    materialsAdapter.onClick { material, position ->
      startActivity<ContentActivity>(
        ContentActivity.EXTRA_MATERIAL to material,
        ContentActivity.EXTRA_POSITION to position
      )
    }
  }
}