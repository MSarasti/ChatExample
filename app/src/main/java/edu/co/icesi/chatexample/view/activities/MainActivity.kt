package edu.co.icesi.chatexample.view.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import edu.co.icesi.chatexample.databinding.ActivityMainBinding
import edu.co.icesi.chatexample.view.ChatAdapter

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.chatRV.adapter = ChatAdapter()
        binding.chatRV.layoutManager = LinearLayoutManager(this)
        binding.chatRV.setHasFixedSize(true)
    }
}