package vcmsa.projects.prog7313_poe_group_10

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class ExpenseAdapter(private val expenses: List<ExpenseEntity>) :
    RecyclerView.Adapter<ExpenseAdapter.ExpenseViewHolder>() {

    class ExpenseViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvExpenseName)
        val tvAmount: TextView = itemView.findViewById(R.id.tvExpenseAmount)
        val tvCategory: TextView = itemView.findViewById(R.id.tvExpenseCategory)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpenseViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_expense, parent, false)
        return ExpenseViewHolder(view)
    }

    override fun onBindViewHolder(holder: ExpenseViewHolder, position: Int) {
        val expense = expenses[position]
        holder.tvName.text = expense.name
        holder.tvAmount.text = "R${String.format("%.2f", expense.amount)}"
        holder.tvCategory.text = expense.category

        holder.itemView.setOnClickListener {
            expense.photoUri?.let { uri ->
                val context = holder.itemView.context
                val intent = Intent(context, ImageViewActivity::class.java).apply {
                    putExtra("image_uri", uri)
                }
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount() = expenses.size
}
