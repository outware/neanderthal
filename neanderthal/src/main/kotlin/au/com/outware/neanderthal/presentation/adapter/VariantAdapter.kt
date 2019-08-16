package au.com.outware.neanderthal.presentation.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckedTextView
import au.com.outware.neanderthal.presentation.presenter.VariantListPresenter
import java.util.*

/**
 * @author timmutton
 */
 class VariantAdapter(var clickListener: (String, Int) -> Unit) :
        RecyclerView.Adapter<VariantAdapter.ViewHolder>(), VariantListPresenter.AdapterSurface {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent?.context)
            .inflate(android.R.layout.simple_list_item_single_choice, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        viewHolder?.checkedTextView?.text = variants[position]
        viewHolder?.checkedTextView?.isChecked = (position == checkedIndex)
        viewHolder?.checkedTextView?.setOnClickListener({
            clickListener(variants[position], position)
        })
    }

    val variants: ArrayList<String> = ArrayList()
    var checkedIndex = 0

    override fun getItemCount(): Int {
        return variants.size
    }

    override fun setCurrentPosition(position: Int) {
        checkedIndex = position
        notifyDataSetChanged()
    }

    override fun add(variants: List<String>) {
        this.variants.removeAll(this.variants)
        this.variants.addAll(variants)
        notifyDataSetChanged()
    }

    override fun add(variant: String) {
        variants.add(variant)
        notifyItemInserted(variants.indexOf(variant))
    }

    override fun remove(index: Int) {
        variants.removeAt(index)
        notifyItemRemoved(index)
    }

    class ViewHolder(val view: View): RecyclerView.ViewHolder(view) {
        val checkedTextView: CheckedTextView
        init {
            checkedTextView = view as CheckedTextView
        }
    }
}