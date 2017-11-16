package com.example.v001ff.footmark;

/**
 * Created by enPiT-P20 on 2017/11/09.
 */

//保留
public class CustomPageAdapter /*extends PagerAdapter*/ {
    /*public final static int N = 5;
    private LayoutInflater _inflater = null;

    public CustomPageAdapter(Context c) {
        super();
        _inflater = (LayoutInflater) c.getSystemServise(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        LinearLayout layout = (LinearLayout) _inflater.inflate(R.layout.page, null);
        int brt = 255*position/N;
        layout.setBackgroundColor(Color.rgb(brt,brt,brt));
        ImageView img = (ImageView) layout.findViewById(R.id.img_scroll);
        int rsrc[] = { R.drawable.img00, R.drawable.img01, R.drawable.img02, R.drawable.img03, R.drawable.img04};
        img.setImageResource(rsrc[position]);
        container.addView(layout);
        return layout;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager) container).remove((View) object);
    }

    @Override
    public int getCount() {
        return N;
    }

    @Override
    public boolean isViewFromObject(View view, Object object){
        return view.equals(object);
    }
    */
}
