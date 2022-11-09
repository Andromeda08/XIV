package hu.tsukiakari.xiv.characterList.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import hu.tsukiakari.xiv.R
import hu.tsukiakari.xiv.characterList.data.SavedCharacter
import hu.tsukiakari.xiv.databinding.ItemCharacterBinding

class SavedCharacterAdapter(
    private val listener: OnCharacterSelectedListener
) : RecyclerView.Adapter<SavedCharacterAdapter.SavedCharacterViewHolder>() {
    private val characters: MutableList<SavedCharacter> = ArrayList()

    interface OnCharacterSelectedListener {
        fun onCharacterSelected(character: SavedCharacter?)
        fun onCharacterDeleted(character: SavedCharacter?)
    }

    inner class SavedCharacterViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var binding = ItemCharacterBinding.bind(itemView)
        private var item: SavedCharacter? = null
        init {
            binding.root.setOnClickListener { listener.onCharacterSelected(item) }
            binding.removeCharacterBtn.setOnClickListener { listener.onCharacterDeleted(item) }
        }

        fun bind(newCharacter: SavedCharacter?) {
            item = newCharacter
            binding.characterName.text = item!!.name
            binding.characterDetails.text = "Lv. ${ item!!.jobLevel } ${ item!!.jobName}"
            binding.characterServer.text = "@${item!!.server}"
            Glide.with(itemView)
                .load(item!!.avatar)
                .transition(DrawableTransitionOptions().crossFade())
                .into(binding.characterAvatar)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SavedCharacterViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_character, parent, false)
        return SavedCharacterViewHolder(view)
    }

    override fun onBindViewHolder(holder: SavedCharacterViewHolder, position: Int) {
        val item = characters[position]
        holder.bind(item)
    }

    override fun getItemCount() = characters.size

    fun initializeList(list: List<SavedCharacter>) {
        characters.clear()
        characters.addAll(list)
        notifyDataSetChanged()
    }

    fun addCharacter(character: SavedCharacter) {
        characters.add(character)
        notifyItemInserted(itemCount - 1)
    }

    fun removeCharacter(character: SavedCharacter) {
        characters.remove(character)
        notifyDataSetChanged()
    }
}