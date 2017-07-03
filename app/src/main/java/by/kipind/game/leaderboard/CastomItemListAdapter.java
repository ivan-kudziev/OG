package by.kipind.game.leaderboard;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import by.kipind.game.olympicgames.R;

public class CastomItemListAdapter extends BaseAdapter {
	static class ViewHolder {
		TextView txt_uid;
		TextView txt_leadPlase;
		ImageView iv_imgID;
		TextView txt_nick;
		TextView txt_progVal;
		TextView txt_progUpDate;

	}

	private static ArrayList<LeaderboardItem> itemDetalesArrayList;

	// private Integer[] imResID;

	private LayoutInflater lInflater;
	private Context adapContext;

	public CastomItemListAdapter(Context context, ArrayList<LeaderboardItem> result) {
		itemDetalesArrayList = result;
		lInflater = LayoutInflater.from(context);
		adapContext = context;
	}

	public int getCount() {
		return itemDetalesArrayList.size();
	}

	public Object getItem(int position) {
		return itemDetalesArrayList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		ViewHolder holder;
		if (convertView == null) {
			convertView = lInflater.inflate(R.layout.leaderboard_listview_item, null);
			holder = new ViewHolder();
			holder.txt_uid = (TextView) convertView.findViewById(R.id.lv_uid);
			holder.txt_leadPlase = (TextView) convertView.findViewById(R.id.lv_leadPlase);
			holder.iv_imgID = (ImageView) convertView.findViewById(R.id.lv_imFlag);
			holder.txt_nick = (TextView) convertView.findViewById(R.id.lv_nick);
			holder.txt_progVal = (TextView) convertView.findViewById(R.id.lv_recValue);
			holder.txt_progUpDate = (TextView) convertView.findViewById(R.id.lv_recDate);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.txt_uid.setText(String.valueOf(itemDetalesArrayList.get(position).getUid()));
		holder.txt_leadPlase.setText(String.valueOf(itemDetalesArrayList.get(position).getLeadPlase()));
	holder.iv_imgID.setImageResource(adapContext.getResources().getIdentifier("flag_" + itemDetalesArrayList.get(position).getCountry().toLowerCase(), "drawable",
		"by.kipind.game.olympicgames")); // itemDetalesArrayList.get(position).getCountry()
		holder.txt_nick.setText(itemDetalesArrayList.get(position).getNick());
		holder.txt_progVal.setText(String.valueOf(itemDetalesArrayList.get(position).getProgVal()));
		holder.txt_progUpDate.setText(itemDetalesArrayList.get(position).getProgUpDate());

		return convertView;

	}

}
