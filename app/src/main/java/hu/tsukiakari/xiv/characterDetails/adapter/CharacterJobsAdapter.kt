package hu.tsukiakari.xiv.characterDetails.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hu.tsukiakari.xiv.R
import hu.tsukiakari.xiv.characterDetails.model.JobExtended
import hu.tsukiakari.xiv.databinding.ItemJobBinding

class CharacterJobsAdapter(private val ctx: Context?): RecyclerView.Adapter<CharacterJobsAdapter.JobViewHolder>()
{
    private val jobs: MutableList<JobExtended> = ArrayList()

    inner class JobViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private var binding = ItemJobBinding.bind(itemView)
        private var job: JobExtended? = null
        fun bind(newJob: JobExtended?) {
            job = newJob
            binding.jobDetails.text = "Lv. ${ job!!.job.level} ${job!!.job.unlockedState.name}"
            binding.jobIcon.setBackgroundResource(job!!.resID)
        }
    }

    fun createList(jobsList: List<JobExtended>) {
        jobsList.forEach { j -> if (j.job.level > 0) jobs.add(j) }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JobViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_job, parent, false)
        return JobViewHolder(view)
    }

    override fun onBindViewHolder(holder: JobViewHolder, position: Int) {
        val item = jobs[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = jobs.size
}