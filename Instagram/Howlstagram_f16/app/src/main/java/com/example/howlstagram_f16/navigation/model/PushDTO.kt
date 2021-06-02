package com.example.howlstagram_f16.navigation.model

data class PushDTO(
    var to : String? =null, //푸쉬를 받는 사람의 아이디(토큰)
    var notification : Notification = Notification()
){
    data class Notification(
        //푸쉬메시지 내용과 제목
    var body : String? = null,
    var title : String? = null
    )
}