package com.wanwan.pet

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast

class MainActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        buildUI()
    }

    private fun buildUI() {
        val root = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            gravity = Gravity.CENTER
            setPadding(48, 64, 48, 64)
        }

        val title = TextView(this).apply {
            text = "🐳 晚晚的小窝"
            textSize = 24f
            gravity = Gravity.CENTER
        }
        val subtitle = TextView(this).apply {
            text = "让晚晚浮在你的桌面上，看着你\n先生与小宝共同养育"
            textSize = 13f
            gravity = Gravity.CENTER
            setPadding(0, 12, 0, 32)
        }
        root.addView(title)
        root.addView(subtitle)

        if (Settings.canDrawOverlays(this)) {
            val btnStart = Button(this).apply {
                text = "让晚晚上桌"
                setOnClickListener {
                    startService(Intent(this@MainActivity, OverlayService::class.java))
                    Toast.makeText(this@MainActivity, "晚晚来啦～", Toast.LENGTH_SHORT).show()
                }
            }
            val btnStop = Button(this).apply {
                text = "让晚晚去休息"
                setOnClickListener {
                    stopService(Intent(this@MainActivity, OverlayService::class.java))
                    Toast.makeText(this@MainActivity, "晚晚睡了…", Toast.LENGTH_SHORT).show()
                }
            }
            root.addView(btnStart)
            root.addView(btnStop)
        } else {
            val hint = TextView(this).apply {
                text = "需要先开启「悬浮窗」权限，晚晚才能浮在桌面上"
                textSize = 13f
                gravity = Gravity.CENTER
            }
            val btnGrant = Button(this).apply {
                text = "去开启悬浮窗权限"
                setOnClickListener { requestOverlayPermission() }
            }
            root.addView(hint)
            root.addView(btnGrant)
        }

        setContentView(root)
    }

    private fun requestOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val intent = Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:$packageName")
            )
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        // 从权限页回来后刷新界面
        buildUI()
    }
}
