package com.example.howlstagram_f16.navigation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.howlstagram_f16.R
import com.example.howlstagram_f16.navigation.model.ContentDTO
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.fragment_detail.view.*
import kotlinx.android.synthetic.main.item_detail.view.*

class DetailViewFragment: Fragment() {
    var firestore : FirebaseFirestore? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = LayoutInflater.from(activity).inflate(R.layout.fragment_detail, container, false)
        firestore = FirebaseFirestore.getInstance()

        view.detailviewfragment_recyclerview.adapter = DetailViewRecyclerViewAdapter()
        view.detailviewfragment_recyclerview.layoutManager = LinearLayoutManager(activity)
        return view
    }

    inner class DetailViewRecyclerViewAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>(){
        var contentDTO : ArrayList<ContentDTO> = arrayListOf()
        var contentUidList : ArrayList<String> = arrayListOf()

        //데이터 받아옴
        init {
            firestore?.collection("images")?.orderBy("timestamp")?.addSnapshotListener { querySnapshot, firebaseFirestoreException ->
                contentDTO.clear()
                contentUidList.clear()
                for(snapshot in querySnapshot!!.documents){
                    var item = snapshot.toObject(ContentDTO::class.java)
                    contentDTO.add(item!!)
                    contentUidList.add(snapshot.id)
                }
                //새로고침
                notifyDataSetChanged()
            }
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view = LayoutInflater.from(parent.context).inflate(R.layout.item_detail,parent,false)
            return CustomViewHolder(view)
        }

        inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view)

        override fun getItemCount(): Int {
            return contentDTO.size
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            var viewholder = (holder as CustomViewHolder).itemView

            //유저 프로필 아이디
            viewholder.detailviewitem_profile_textview.text = contentDTO!![position].userId

            //유저 프로필 이미지
            Glide.with(holder.itemView.context).load(contentDTO!![position].imageUrl).into(viewholder.detailviewitem_profile_image)

            //업로드 이미지
            Glide.with(holder.itemView.context).load(contentDTO!![position].imageUrl).into(viewholder.detailviewitem_imageview_content)

            //업로드 내용
            viewholder.detailviewitem_explain_textview.text = contentDTO!![position].explain

            //좋아요 개수
            viewholder.detailviewitem_favoritecounter_textview.text="Likes "+ contentDTO!![position].favoriteCount


        }
    }
}