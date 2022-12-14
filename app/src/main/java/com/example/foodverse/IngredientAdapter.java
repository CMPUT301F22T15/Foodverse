package com.example.foodverse;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

/**
 * Custom ingredient adapter to hold stored ingredients.
 */
public class IngredientAdapter extends ArrayAdapter<Ingredient> {
    private ArrayList<Ingredient> ingredients;
    private Context context;

    /**
     * Ingredient adapter constructor.
     * @param context The parent activity.
     * @param ingredients The list of ingredients for the adapter.
     */
    public IngredientAdapter(Context context, ArrayList<Ingredient> ingredients) {
        super(context, 0, ingredients);
        this.ingredients = ingredients;
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView,
                        @NonNull ViewGroup parent) {
        View view = convertView;
        if(view == null){
            view = LayoutInflater.from(context).inflate(R.layout.content_ingredient,
                    parent,false);
        }
        Ingredient ingredient = ingredients.get(position);
        TextView ingredientDescription = view.findViewById(
                R.id.description_text);
        TextView ingredientCount = view.findViewById(R.id.count_text);
        TextView ingredientUnit = view.findViewById(R.id.unit_text);

        ingredientDescription.setText(ingredient.getDescription());
        ingredientCount.setText("x" + Integer.toString(ingredient.getCount()));
        ingredientUnit.setText(ingredient.getUnit());
        return view;
    }
}
