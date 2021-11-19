package br.com.iesb.drinkme.view.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import br.com.iesb.drinkme.databinding.DrinkItemRvBinding
import br.com.iesb.drinkme.model.GetListOfDrinks
import coil.load

class DrinkAdapter : ListAdapter<GetListOfDrinks.Drink, DrinkAdapter.DrinkViewHolder>(differCallback) {

    inner class DrinkViewHolder(
        private val binding : DrinkItemRvBinding
    ): RecyclerView.ViewHolder(binding.root){
        fun bind (drink : GetListOfDrinks.Drink){
            binding.drinkImage.load(drink.strDrinkThumb)
            binding.drinkName.text = drink.strDrink

            binding.root.setOnClickListener {
                onItemClickListener?.invoke(drink)
            }

            binding.root.setOnLongClickListener {
                onLongItemClickListener.invoke(drink)
            }
        }
    }

    companion object{
        private val differCallback : DiffUtil.ItemCallback<GetListOfDrinks.Drink> =
            object  : DiffUtil.ItemCallback<GetListOfDrinks.Drink>() {
                override fun areItemsTheSame(
                    oldItem: GetListOfDrinks.Drink,
                    newItem: GetListOfDrinks.Drink
                ): Boolean {
                    return oldItem == newItem
                }

                override fun areContentsTheSame(
                    oldItem: GetListOfDrinks.Drink,
                    newItem: GetListOfDrinks.Drink
                ): Boolean {
                    return oldItem.idDrink == newItem.idDrink
                }
            }
    }

    val differ = AsyncListDiffer(this, differCallback)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DrinkAdapter.DrinkViewHolder {
        val binding = DrinkItemRvBinding.inflate(
            LayoutInflater.from(parent.context),parent,false
        )
        return DrinkViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DrinkAdapter.DrinkViewHolder, position: Int) {
        holder.bind(differ.currentList[position])
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }

    private var onItemClickListener : ((GetListOfDrinks.Drink) -> Unit)? = null

    fun setOnClickListener(clickListener: (GetListOfDrinks.Drink) -> Unit){
        onItemClickListener = clickListener
    }

    private lateinit var onLongItemClickListener : ((GetListOfDrinks.Drink) -> Boolean)

    fun setOnLongItemClickListener(longClickListener: (GetListOfDrinks.Drink) -> Boolean){
        onLongItemClickListener = longClickListener
    }

}
