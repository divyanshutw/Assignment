
import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.assignment.physicswallah.R
import com.assignment.physicswallah.database.teachers.Teacher
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class TeacherAdapter(
    var itemList: ArrayList<Teacher>) :
    RecyclerView.Adapter<TeacherAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.teacher_item_view, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageView = holder.itemView.findViewById<ImageView>(R.id.imageView_profileImage)
        Glide.with(holder.itemView.context)
            .load(itemList[position].profileImage)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image)
            )
            .into(imageView)

        holder.bind(itemList[position])
    }

    class ViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        init {}

        @SuppressLint("SetTextI18n")
        fun bind(teacher: Teacher) {
            val textView = itemView.findViewById<TextView>(R.id.textView1)
            textView.text = teacher.name;
            val textViewSubject = itemView.findViewById<TextView>(R.id.textView_subject)
            textViewSubject.text = teacher.subjects[0].toString();
            val textViewQualification = itemView.findViewById<TextView>(R.id.textView_qualification)
            textViewQualification.text = "⚫  ${teacher.qualification[0]}";

        }
    }

}