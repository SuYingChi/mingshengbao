package com.msht.minshengbao.androidShop.adapter;

import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.msht.minshengbao.R;
import com.msht.minshengbao.androidShop.activity.ShopOrderRouteActivity;
import com.msht.minshengbao.androidShop.util.RecyclerHolder;

public class ShopRouteAdapter extends MyHaveHeadAndFootRecyclerAdapter<String>{

    public ShopRouteAdapter(Context context) {
        super(context, R.layout.item_shop_route);
    }

    @Override
    public void convert(RecyclerHolder holder, String s, int position) {
        View point= holder.getView(R.id.point);
        View topline = holder.getView(R.id.topLine);
        View bottomLine = holder.getView(R.id.bottomLine);
        int firstPosition = getFirstMatchingIndex(s, "&nbsp;");
        TextView tv = holder.getView(R.id.info);
        int lastPosition = getLastMatchingIndex(s, "&nbsp;");
        TextView tv2 = holder.getView(R.id.info2);
        tv.setText(s.substring(lastPosition+6));
        tv2.setText(s.substring(0,firstPosition));
        if(datas.size()>1) {
            if (position == 0) {
                point.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.shop_page_circle_on));
                topline.setVisibility(View.INVISIBLE);
                bottomLine.setVisibility(View.VISIBLE);
                tv.setTextColor(context.getResources().getColor(R.color.msb_color));
                tv2.setTextColor(context.getResources().getColor(R.color.msb_color));
            } else if (position > 0 && position < datas.size() - 1) {
                point.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.empty_circle));
                topline.setVisibility(View.VISIBLE);
                bottomLine.setVisibility(View.VISIBLE);
                tv.setTextColor(context.getResources().getColor(R.color.shop_grey));
                tv2.setTextColor(context.getResources().getColor(R.color.shop_grey));
            } else if (position == datas.size() - 1) {
                point.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.empty_circle));
                topline.setVisibility(View.VISIBLE);
                bottomLine.setVisibility(View.INVISIBLE);
                tv.setTextColor(context.getResources().getColor(R.color.shop_grey));
                tv2.setTextColor(context.getResources().getColor(R.color.shop_grey));
            }
        }else if(datas.size()==1){
            point.setBackgroundDrawable(context.getResources().getDrawable(R.drawable.shop_page_circle_on));
            topline.setVisibility(View.INVISIBLE);
            bottomLine.setVisibility(View.INVISIBLE);
            tv.setTextColor(context.getResources().getColor(R.color.msb_color));
            tv2.setTextColor(context.getResources().getColor(R.color.msb_color));
        }
    }
    private   int getFirstMatchingIndex(String input, String query) {
        char[] inputChars = input.toCharArray();
        char[] queryChars = query.toCharArray();
        int inputLength = input.length();
        int queryLength = query.length();

        int inputIndex = 0;
        int queryIndex = 0;
        while (inputIndex < inputLength && queryIndex < queryLength) {
            if (inputChars[inputIndex] == queryChars[queryIndex]) {
                queryIndex++;
                inputIndex++;
            } else {
                inputIndex = inputIndex - queryIndex + 1;
                queryIndex = 0;
            }
        }

        int index = queryIndex == queryLength ? (queryLength > 1 ? inputIndex - queryLength : inputIndex - 1) : -1;
        return index;
    }
    private  int getLastMatchingIndex(String input, String query) {
        char[] inputChars = input.toCharArray();
        char[] queryChars = query.toCharArray();
        int inputLength = input.length();
        int queryLength = query.length();
        int inputIndex = inputLength - 1;
        int queryIndex = queryLength - 1;
        int matchingLenght = 0;
        while (inputIndex >= 0 && queryIndex >= 0) {
            if (inputChars[inputIndex] == queryChars[queryIndex]) {
                inputIndex--;
                queryIndex--;
                matchingLenght++;
            } else {
                inputIndex = matchingLenght <= 0 ? (inputIndex - 1) : inputIndex;
                queryIndex = queryLength - 1;
                matchingLenght = 0;
            }
        }
       return matchingLenght == queryLength ? (inputIndex + 1) : -1;
    }
}
