package com.tecnosols.parathapoint;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {
    ViewFlipper viewFlipper;
    RecyclerView horizontalRecyclerView1;
    List<HorizontalModel> DataList = new ArrayList<>();
    RecyclerView horizontalRecyclerView2;
    List<HorizontalModelHotPics> DataList2 = new ArrayList<>();
    GridView gridView;
    DatabaseReference dref, databaseReference, databaseReference2;
    ProgressDialog pd;
    List<HorizontalModel> DataList3 = new ArrayList<>();

    private CardView card_veg,card_nonveg,card_pizza,card_combo,card_platter,card_meal;


    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        dref = FirebaseDatabase.getInstance().getReference().child("DealsOfTheDay");
        databaseReference = FirebaseDatabase.getInstance().getReference().child("HotPics");
        databaseReference2 = FirebaseDatabase.getInstance().getReference().child("WhatsNew");

        pd = new ProgressDialog(container.getContext());
        pd.setMessage("Loading.. Please   Wait!");
        pd.setCancelable(false);
        pd.show();

        int[] images = {R.drawable.img1, R.drawable.img2, R.drawable.img3, R.drawable.img4, R.drawable.img5};
        viewFlipper = view.findViewById(R.id.viewFlipper);
        horizontalRecyclerView1 = view.findViewById(R.id.recyclerViewHorizontalLayout1);
        horizontalRecyclerView2 = view.findViewById(R.id.recyclerViewHorizontalLayout2);
        gridView = view.findViewById(R.id.gridView);
        card_veg=view.findViewById(R.id.cardView_veg);
        card_nonveg=view.findViewById(R.id.cardView_non_veg);
        card_pizza=view.findViewById(R.id.card_pizza);
        card_combo=view.findViewById(R.id.cradView_combo);
        card_platter=view.findViewById(R.id.cardView_platter);
        card_meal=view.findViewById(R.id.cardView_meal);

        /*for(int i=0;i<images.length;i++){
            fliperImages(images[i]);
        }*/

        for (int image : images) {
            fliperImages(image);
        }


        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(container.getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);

        dref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    item_details ud = ds.getValue(item_details.class);
                    DataList.add(new HorizontalModel(ud.item_picURL, ud.item_name, ud.item_price));

                }

                Collections.reverse(DataList);
                final HorizontalAdapter recyclerAdapter = new HorizontalAdapter(DataList);
                horizontalRecyclerView1.setLayoutManager(linearLayoutManager);
                horizontalRecyclerView1.setAdapter(recyclerAdapter);
                recyclerAdapter.notifyDataSetChanged();


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                pd.cancel();
            }
        });

        horizontalRecyclerView1.addOnItemTouchListener(
                new RecyclerItemClickListener(container.getContext(), horizontalRecyclerView1, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(container.getContext(), ItemDetailActivity.class);
                        intent.putExtra("item_name", DataList.get(position).getItemName());
                        intent.putExtra("item_price", DataList.get(position).getItemPrice());
                        intent.putExtra("image_resource", DataList.get(position).getImageResource());
                        // intent.putExtra("image_desc",DataList.get(i).get)
                        startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );

        databaseReference2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    item_details idt = ds.getValue(item_details.class);
                    DataList3.add(new HorizontalModel(idt.item_picURL, idt.item_name, idt.item_price));

                }
                CustomAdapter customAdapter = new CustomAdapter();
                gridView.setAdapter(customAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(container.getContext(), ItemDetailActivity.class);
                intent.putExtra("item_name", DataList3.get(i).getItemName());
                intent.putExtra("item_price", DataList3.get(i).getItemPrice());
                intent.putExtra("image_resource", DataList3.get(i).getImageResource());
                startActivity(intent);

            }
        });

        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                pd.cancel();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    item_details id = ds.getValue(item_details.class);
                    DataList2.add(new HorizontalModelHotPics(id.item_picURL, id.item_name, id.item_price));
                }
                HorizontalAdapterHotPics recyclerAdapter2 = new HorizontalAdapterHotPics(DataList2);
                LinearLayoutManager linearLayoutManager2 = new LinearLayoutManager(container.getContext());
                linearLayoutManager2.setOrientation(LinearLayoutManager.HORIZONTAL);

                horizontalRecyclerView2.setLayoutManager(linearLayoutManager2);
                horizontalRecyclerView2.setAdapter(recyclerAdapter2);
                recyclerAdapter2.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        horizontalRecyclerView2.addOnItemTouchListener(
                new RecyclerItemClickListener(container.getContext(), horizontalRecyclerView1, new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Intent intent = new Intent(container.getContext(), ItemDetailActivity.class);
                        intent.putExtra("item_name", DataList2.get(position).getImageTitle());
                        intent.putExtra("item_price", DataList2.get(position).getImagePrice());
                        intent.putExtra("image_resource", DataList2.get(position).getImageResoure());
                        // intent.putExtra("image_desc",DataList.get(i).get)
                        startActivity(intent);
                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }
                })
        );

        card_veg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(container.getContext(),MenuActivity_Veg.class);
                intent.putExtra("flag","veg_card");
                startActivity(intent);
            }
        });
        card_nonveg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(container.getContext(),MenuActivity_Veg.class);
                intent.putExtra("flag","non_veg_card");
                startActivity(intent);
            }
        });
        card_pizza.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(container.getContext(),MenuActivity_Veg.class);
                intent.putExtra("flag","pizza_card");
                startActivity(intent);
            }
        });
        card_combo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(container.getContext(),MenuActivity_Veg.class);
                intent.putExtra("flag","combo_card");
                startActivity(intent);
            }
        });
        card_platter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(container.getContext(),MenuActivity_Veg.class);
                intent.putExtra("flag","platter_card");
                startActivity(intent);
            }
        });
        card_meal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(container.getContext(),MenuActivity_Veg.class);
                intent.putExtra("flag","meal_card");
                startActivity(intent);
            }
        });


        return view;
    }

    private void fliperImages(int image) {
        ImageView imageView = new ImageView(getContext());
        imageView.setBackgroundResource(image);
        //imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);

        viewFlipper.addView(imageView);
        viewFlipper.setFlipInterval(3000);
        viewFlipper.setAutoStart(true);

        viewFlipper.setInAnimation(getContext(), android.R.anim.slide_in_left);
        viewFlipper.setOutAnimation(getContext(), android.R.anim.slide_out_right);
    }

    private class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return DataList3.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View view1 = getLayoutInflater().inflate(R.layout.grid_layout, null);
            ImageView pic = view1.findViewById(R.id.gsProductImage);
            TextView name = view1.findViewById(R.id.gsProductTitle);
            TextView price = view1.findViewById(R.id.gsProductPrice);

            Picasso.get().load(DataList3.get(i).getImageResource()).into(pic);
            name.setText(DataList3.get(i).getItemName());
            price.setText(" ₹" + DataList3.get(i).getItemPrice());


            return view1;
        }
    }

}
