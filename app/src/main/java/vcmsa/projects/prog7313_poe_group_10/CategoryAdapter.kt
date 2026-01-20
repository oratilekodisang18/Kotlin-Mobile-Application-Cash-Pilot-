package vcmsa.projects.prog7313_poe_group_10

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class CategoryAdapter(private var categories: List<CategoryEntity>) :
    RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    inner class CategoryViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvCategoryName)
        val tvType: TextView = itemView.findViewById(R.id.tvCategoryType)
        val tvLimit: TextView = itemView.findViewById(R.id.tvMonthlyLimit)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_category, parent, false)
        return CategoryViewHolder(view)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val category = categories[position]
        holder.tvName.text = category.name
        holder.tvType.text = category.type
        holder.tvLimit.text = category.monthlyLimit?.let { "Limit: R$it" } ?: "No Limit"
    }

    override fun getItemCount() = categories.size

    fun updateData(newList: List<CategoryEntity>) {
        categories = newList
        notifyDataSetChanged()
    }
}
