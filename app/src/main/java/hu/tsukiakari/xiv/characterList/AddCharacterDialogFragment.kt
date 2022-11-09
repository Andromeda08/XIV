package hu.tsukiakari.xiv.characterList

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import androidx.fragment.app.DialogFragment
import hu.tsukiakari.xiv.R
import hu.tsukiakari.xiv.databinding.DialogAddCharacterBinding
import java.lang.RuntimeException

class AddCharacterDialogFragment : DialogFragment() {
    interface AddCharacterDialogListener {
        fun onCharacterAdded(name: String, server: String)
    }

    private lateinit var binding: DialogAddCharacterBinding
    private lateinit var listener: AddCharacterDialogListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
        binding = DialogAddCharacterBinding.inflate(LayoutInflater.from(context))
        listener = context as? AddCharacterDialogListener ?: throw RuntimeException(":D")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(requireContext())
            .setTitle(R.string.add_character_title)
            .setView(binding.root)
            .setPositiveButton(R.string.add_character_add_btn) { _, _ ->
                listener.onCharacterAdded(
                    binding.addCharacterName.text.toString(),
                    binding.addCharacterServer.text.toString()
                )
            }
            .setNegativeButton(R.string.add_character_cancel_btn, null)
            .create()
    }
}