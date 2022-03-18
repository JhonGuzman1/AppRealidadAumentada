package com.guzman.realidadvirtual.ADAPTER;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.guzman.realidadvirtual.ACTIVITIES.RealidadVirtualActivity;
import com.guzman.realidadvirtual.CLASS.ImagenClass;
import com.guzman.realidadvirtual.R;
import com.guzman.realidadvirtual.UTILS.PARAMETROSGLOBALES;

import java.util.ArrayList;
import java.util.List;

public class ImagenAdapter extends RecyclerView.Adapter<ImagenAdapter.CustomViewHolder> implements Filterable {


    Context context;
    private List<ImagenClass> ImagenesList;
    private List<ImagenClass> ImagenesFilterList;


    public ImagenAdapter(Context context, List<ImagenClass> list ) {
        this.context = context;
        ImagenesList = list;
        ImagenesFilterList = list;



    }

    @Override
    public ImagenAdapter.CustomViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_imagenes,parent, false);
        CustomViewHolder viewHolder = new CustomViewHolder(view);
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(ImagenAdapter.CustomViewHolder holder, final int position) {
        final ImagenClass imagenClass = ImagenesFilterList.get(position);

        holder.tvNombre.setText(imagenClass.getNombre());

        switch (imagenClass.getIdImagen()){
            case 1 :
                holder.ivImagen.setImageResource(R.drawable.personaje01);
                break;
            case 2:
                holder.ivImagen.setImageResource(R.drawable.personaje02);
                break;
            case 3:
                holder.ivImagen.setImageResource(R.drawable.personaje03);
                break;
        }



        holder.cvContenedor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PARAMETROSGLOBALES.IdImagen = imagenClass.getIdImagen();
                Intent intent = new Intent(context, RealidadVirtualActivity.class);
                context.startActivity(intent);

            }
        });

    }



    public void Clear() {
        if (ImagenesFilterList.size() > 0) {
            ImagenesFilterList.clear();
            notifyDataSetChanged();
        }

    }

    @Override
    public int getItemCount() {
        return ImagenesFilterList.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                ArrayList<ImagenClass> filteredList;
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    filteredList = new ArrayList<>();
                    ImagenesFilterList = ImagenesList;
                } else {
                    filteredList = new ArrayList<>();
                    for (ImagenClass publicacion : ImagenesList) {
                        if (true) {
                            filteredList.add(publicacion);
                        }
                    }
                    ImagenesFilterList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = ImagenesFilterList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                ImagenesFilterList = (ArrayList<ImagenClass>) filterResults.values;
                notifyDataSetChanged();
            }
        };

    }

    class CustomViewHolder extends RecyclerView.ViewHolder {

        CardView cvContenedor;
        ImageView ivImagen;
        TextView tvNombre;

        public CustomViewHolder(View view) {
            super(view);

            cvContenedor = itemView.findViewById(R.id.cv_card_imagenes);
            ivImagen = itemView.findViewById(R.id.iv_imagen_card_imagenes);
            tvNombre = itemView.findViewById(R.id.tv_nombre_card_imagenes);


        }

    }


}
