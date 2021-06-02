package com.codepolitan.belajarngoding.presentation.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.inputmethod.EditorInfo
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import com.codepolitan.belajarngoding.R
import com.codepolitan.belajarngoding.adapter.MaterialsAdapter
import com.codepolitan.belajarngoding.databinding.ActivityMainBinding
import com.codepolitan.belajarngoding.presentation.content.ContentActivity
import com.codepolitan.belajarngoding.presentation.user.UserActivity
import com.codepolitan.belajarngoding.repository.Repository
import com.codepolitan.belajarngoding.utils.showDialogLoading
import org.jetbrains.anko.startActivity

class MainActivity : AppCompatActivity() {

  companion object{
    const val EXTRA_POSITION = "extra_position"
  }
  private lateinit var mainBinding: ActivityMainBinding
  private lateinit var materialsAdapter: MaterialsAdapter

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    mainBinding = ActivityMainBinding.inflate(layoutInflater)
    setContentView(mainBinding.root)

    //Init
    materialsAdapter = MaterialsAdapter()

    getDataMaterial()
    onAction()
  }

  override fun onResume() {
    super.onResume()
    if (intent != null){
      val position = intent.getIntExtra(EXTRA_POSITION, 0)
      mainBinding.rvMaterialsMain.smoothScrollToPosition(position)
    }
  }

  private fun getDataMaterial() {
    showLoading()

    val materials = Repository.getMaterials(this)

    Handler(Looper.getMainLooper())
      .postDelayed({
        hideLoading()
        materials?.let {
          materialsAdapter.materials = it
        }
      }, 1200)

    mainBinding.rvMaterialsMain.adapter = materialsAdapter
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
        getDataMaterial()
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