package com.example.to_do_list

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform