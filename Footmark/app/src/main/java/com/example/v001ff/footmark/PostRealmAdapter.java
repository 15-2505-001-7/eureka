package com.example.v001ff.footmark;

/**
 * Created by enPiT-P7 on 2017/11/16.
 */

/*public class PostRealmAdapter  extends RealmRecyclerViewAdapter<Post, PostRealmAdapter.PostViewHolder> {
    Context context;

    public static class PostViewHolder extends RecyclerView.ViewHolder{
        protected TextView userName;
        protected TextView spoyInfo;
        protected TextView date;
        protected ImageView userPhoto;

        public PostViewHolder(View itemView){
            super(itemView);
            userName = (TextView) itemView.findViewById(R.id.username);
            spoyInfo = (TextView) itemView.findViewById(R.id.spotinfo);
            date = (TextView) itemView.findViewById(R.id.date);
            userPhoto = (ImageView) itemView.findViewById(R.id.userphoto);
        }
    }

    public PostRealmAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<Post> data,
                             boolean autoUpdate) {
        super(data, autoUpdate);
        this.context = context;
    }

    @Override
    public SpotViewHolder onCreateViewHolder(ViewGroup parent, int viewType){
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_layout, parent, false);
        final SpotViewHolder holder = new SpotViewHolder(itemView);

        /*holder.itemView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                int position = holder.getAdapterPosition();
                Post post = getData().get(position);
                long postId = post.id;

                Intent intent = new Intent(context, ShowSpotActivity.class);
                intent.putExtra(ShowSpotActivity.DIARY_ID, diaryId);
                context.startActivity(intent);
            }
        });*/
/*        return holder;
    }

    @Override
    public void onBindViewHolder(SpotViewHolder holder, int position){
        Spot spot = getData().get(position);
        holder.userName.setText(spot.username);
        holder.spoyInfo.setText(spot.spotinfo);
        holder.date.setText(spot.date);
        if(spot.image != null && spot.image.length != 0){
            Bitmap bmp = MyUtils.getImageFromByte(spot.image);
            holder.spot.setImageBitmap(bmp);
        }
    }
}*/