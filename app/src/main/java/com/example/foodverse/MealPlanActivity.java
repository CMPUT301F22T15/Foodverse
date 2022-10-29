package com.example.foodverse;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.Timestamp;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class MealPlanActivity extends AppCompatActivity implements
        MealPlanFragment.OnFragmentInteractionListener,
        NavigationView.OnNavigationItemSelectedListener {

    private ListView mealListView;
    private ArrayAdapter<Meal> mealAdapter;
    //private ArrayList<Meal> mealList;
    private FirebaseFirestore db;
    private final String TAG = "MealPlanActivity";
    private CollectionReference collectionReference;
    private ArrayList<Meal> mealArrayList;
    private int selectedMealIndex;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private DrawerLayout drawerLayout;
    private NavigationView navView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal_plan);

        selectedMealIndex = -1;
        mealListView = findViewById(R.id.meal_list);

        // Initialize attributes
        mealArrayList = new ArrayList<>();
        mealAdapter = new MealList(this, mealArrayList);
        mealListView.setAdapter(mealAdapter);

        /*
         * https://www.geeksforgeeks.org/navigation-drawer-in-android/
         * by adityamshidlyali, 2020
         */
        drawerLayout = findViewById(R.id.meal_plan_drawer);
        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.nav_open, R.string.nav_close);

        // Allow menu to be toggleable, always display.
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Setup listeners for the navigation view
        navView = findViewById(R.id.nav_menu_meals);
        navView.setNavigationItemSelectedListener(this);

        // Get db, the MealPlan collection
        db = FirebaseFirestore.getInstance();

        collectionReference = db.collection("MealPlan");

        collectionReference.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable
                    FirebaseFirestoreException error) {
                // Clear the old list
                mealArrayList.clear();
                // Add ingredients from the cloud
                for(QueryDocumentSnapshot doc: queryDocumentSnapshots) {
                    Log.d(TAG, String.valueOf(doc.getId()));
                    String hashCode = doc.getId();
                    // TODO: This needs testing
                    ArrayList<String> ingStrings = (ArrayList<String>)
                            doc.getData().get("Ingredients");
                    // Reconstruct ArrayList
                    ArrayList<Ingredient> ingredients = new ArrayList<>();
                    for (String ingString : ingStrings) {
                        Ingredient ing =
                                DatabaseIngredient.stringToIngredient(ingString);
                        ingredients.add(ing);
                    }
                    //mealArrayList.add(new Meal(ingredients, date));
                }
                // Update with new cloud data
                mealAdapter.notifyDataSetChanged();
            }
        });

        ArrayList<Ingredient> ing = new ArrayList<>();
        ing.add(new Ingredient("1", 1));
        ing.add(new Ingredient("2", 2));
        mealAdded(new Meal(ing, new Date()));
        mealListView.setOnItemClickListener((adapterView, view, i, l) -> selectedMealIndex = i);

        final Button addMealButton = findViewById(R.id.add_meal_button);

        addMealButton.setOnClickListener( new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new MealPlanFragment().show(getSupportFragmentManager(), "TEST");
            }
        });

        mealListView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view,
                                            int position, long id) {
                        Meal meal = mealAdapter.getItem(position);
                        selectedMealIndex = position;
                        new MealPlanFragment(meal).show(
                                getSupportFragmentManager(), "EDIT_MEAL");
                    }
                });


    }

    /**
     * Called when the user chooses to add a meal to their list.
     * The function adds the meal using the {@link Meal#hashCode()} as the key
     * for the {@link Meal} object for the database.
     *
     * @param meal The {@link Meal} object to add to the database.
     */
    public void mealAdded(Meal meal) {
        HashMap<String, Object> data = new HashMap<>();
        // Grab data from the ingredient object


        // Can't store ingredient directly so use DatabaseIngredient methods
        ArrayList<String> ingStrings = new ArrayList<>();
        String ingString;
        for (int i = 0; i < meal.getIngredients().size(); i++) {
            ingString = DatabaseIngredient.ingredientToString(
                    meal.getIngredients().get(i));
            ingStrings.add(ingString);
        }


        data.put("Ingredients", ingStrings);
        /*
         * Store all data under the hash code of the meal, so we can
         * store multiple similar meals.
         */
        collectionReference
                .document(String.valueOf(meal.hashCode()))
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // These are a method which gets executed when the task is succeeded
                        Log.d(TAG, "Data has been added successfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // These are a method which gets executed if there’s any problem
                        Log.d(TAG, "Data could not be added!" + e.toString());
                    }
                });
    }

    /**
     * Called when the user chooses to delete a meal from their list.
     * The function removes the meal with equal {@link Meal#hashCode()} from
     * the Firestore database.
     */
    public void mealDeleted() {
        if (selectedMealIndex != -1) {
            Meal oldMeal = mealArrayList.get(selectedMealIndex);
            // Remove ingredient from database
            collectionReference
                    .document(String.valueOf(oldMeal.hashCode()))
                    .delete()
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            // Log success
                            Log.d(TAG, "Data has been deleted!");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // Log any issues
                            Log.d(TAG, "Data could not be deleted!" + e.toString());
                        }
                    });

            // Change the index to be invalid
            selectedMealIndex = -1;
        }
    }

    /**
     * Called when the user confirms new edits in a meal. This function removes
     * the old {@link Meal} from Firestore and adds a new one to the database
     * so that {@link Meal#hashCode()} references remain up to date.
     *
     * @param meal The {@link Meal} object that was edited.
     */
    public void mealEdited(Meal meal) {
        HashMap<String, Object> data = new HashMap<>();
        Meal oldMeal = mealArrayList.get(selectedMealIndex);

        // Grab data from the updated ingredient
        // Can't store ingredient directly so use DatabaseIngredient methods
        ArrayList<String> ingStrings = new ArrayList<>();
        for (Ingredient ingredient : meal.getIngredients()) {
            String ingString = DatabaseIngredient.ingredientToString(ingredient);
            ingStrings.add(ingString);
        }
        data.put("Ingredients", Arrays.asList(ingStrings));

        // Delete old ingredient and set new since hashCode() will return different result
        collectionReference.document(String.valueOf(oldMeal.hashCode()))
                .delete();
        collectionReference
                .document(String.valueOf(meal.hashCode()))
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Log success
                        Log.d(TAG, "Data has been updated successfully!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Log any issues
                        Log.d(TAG, "Data could not be updated!" + e.toString());
                    }
                });
    }


    /**
     * Implemented to allow for the opening and closing of the navigation menu.
     *
     * Code from: https://www.geeksforgeeks.org/navigation-drawer-in-android/
     * By adityamshidlyali, posted 2020, accessed October 28, 2022.
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (actionBarDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * Overridden from NavigationView.OnNavigationItemSelectedListener.
     * Navigate to the selected activity, if we are not already on it, otherwise
     * close the menu. Possible destinations are {@link StoredIngredientActivity},
     * {@link MealPlanActivity}, and {@link ShoppingListActivity}.
     *
     * Code inspired by: https://stackoverflow.com/questions/42297381/onclick-event-in-navigation-drawer
     * Post by Grzegorz (2017) edited by ElOjcar (2019). Accessed Oct 28, 2022.
     *
     * @returns Always true, iff the selected item is the calling activity.
     */
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menu) {
        // Go to activity selected, based on title.
        String destination = (String) menu.getTitle();
        switch(destination) {
            /*case "Recipes": {

            }*/
            case "Ingredients": {
                Intent intent = new Intent(this, StoredIngredientActivity.class);
                startActivity(intent);
                break;
            }
            case "Shopping List": {
                Intent intent = new Intent(this, ShoppingListActivity.class);
                startActivity(intent);
                break;
            }
            default: break;
        }

        // Close navigation drawer if we selected the current activity.
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
}