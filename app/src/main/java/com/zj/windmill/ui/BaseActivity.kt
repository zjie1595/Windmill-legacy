package com.zj.windmill.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.ViewDataBinding
import com.zj.windmill.BaseBinding
import com.zj.windmill.getViewBinding

abstract class BaseActivity<VB : ViewDataBinding> : AppCompatActivity(), BaseBinding<VB> {

    internal val binding: VB by lazy(mode = LazyThreadSafetyMode.NONE) {
        getViewBinding(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        binding.initBinding()
        supportActionBar?.setDisplayHomeAsUpEnabled(enableBackPress())
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val menuRes = menuRes() ?: return false
        menuInflater.inflate(menuRes, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressedDispatcher.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    /**
     * 设置要加载的菜单资源id，默认为空，也就是默认不加载菜单
     */
    open fun menuRes(): Int? = null

    /**
     * 是否启用返回键
     */
    open fun enableBackPress(): Boolean {
        return true
    }
}