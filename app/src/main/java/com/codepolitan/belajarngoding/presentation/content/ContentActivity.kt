package com.codepolitan.belajarngoding.presentation.content

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.codepolitan.belajarngoding.R
import com.codepolitan.belajarngoding.databinding.ActivityContentBinding

class ContentActivity : AppCompatActivity() {

    private lateinit var contentBinding: ActivityContentBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        contentBinding = ActivityContentBinding.inflate(layoutInflater)
        setContentView(contentBinding.root)
    }
}