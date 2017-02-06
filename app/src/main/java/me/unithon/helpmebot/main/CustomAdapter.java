package me.unithon.helpmebot.main;

import android.content.Context;
import android.media.Image;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

import de.hdodenhof.circleimageview.CircleImageView;
import me.unithon.helpmebot.R;

/**
 * Created by kinamare on 2017-02-05.
 */

public class CustomAdapter extends BaseAdapter {
	public class ListContents {
		String msg;
		int type;

		ListContents(String _msg, int _type) {
			this.msg = _msg;
			this.type = _type;
		}
	}

	private ArrayList<ListContents> m_List;

	public CustomAdapter() {
		m_List = new ArrayList<ListContents>();
	}

	public void add(String _msg, int _type) {

		m_List.add(new ListContents(_msg, _type));
	}


	public void remove(int _position) {
		m_List.remove(_position);
	}

	@Override
	public int getCount() {
		return m_List.size();
	}

	@Override
	public Object getItem(int position) {
		return m_List.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final int pos = position;
		final Context context = parent.getContext();

		TextView text = null;
		CustomHolder holder = null;
		LinearLayout layout = null;
		View viewRight = null;
		View viewLeft = null;
		CircleImageView imageView = null;
		Random random = new Random();


		if (convertView == null) {

			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.activity_chatitem, parent, false);

			layout = (LinearLayout) convertView.findViewById(R.id.layout);
			text = (TextView) convertView.findViewById(R.id.text);
			viewRight = convertView.findViewById(R.id.imageViewright);
			viewLeft = convertView.findViewById(R.id.imageViewleft);
			imageView = (CircleImageView) convertView.findViewById(R.id.categoryImage);


			holder = new CustomHolder();
			holder.m_TextView = text;
			holder.layout = layout;
			holder.viewRight = viewRight;
			holder.viewLeft = viewLeft;
			holder.imageView = imageView;
			convertView.setTag(holder);
		} else {
			holder = (CustomHolder) convertView.getTag();
			text = holder.m_TextView;
			layout = holder.layout;
			viewRight = holder.viewRight;
			viewLeft = holder.viewLeft;
			imageView = holder.imageView;

		}

		text.setText(m_List.get(position).msg);



		if (m_List.get(position).type == 0) {
			imageView.setVisibility(View.VISIBLE);
			imageView.setBackgroundResource(img[random.nextInt(4)]);
//			text.setBackgroundResource(R.drawable.categorybox);
			text.setVisibility(View.VISIBLE);
			layout.setGravity(Gravity.LEFT);
			viewRight.setVisibility(View.GONE);
			viewLeft.setVisibility(View.GONE);
		} else if (m_List.get(position).type == 1) {
//			text.setBackgroundResource(R.drawable.categorybox);
			text.setVisibility(View.VISIBLE);
			layout.setGravity(Gravity.RIGHT);
			viewRight.setVisibility(View.GONE);
			imageView.setVisibility(View.GONE);
			viewLeft.setVisibility(View.GONE);
		} else if (m_List.get(position).type == 2) {
//			text.setBackgroundResource(R.drawable.datebg);
			text.setVisibility(View.VISIBLE);
			layout.setGravity(Gravity.CENTER);
			viewRight.setVisibility(View.VISIBLE);
			imageView.setVisibility(View.GONE);
			viewLeft.setVisibility(View.VISIBLE);
		}


		convertView.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Toast.makeText(context, "toast " + m_List.get(pos), Toast.LENGTH_SHORT).show();
			}
		});


		convertView.setOnLongClickListener(new View.OnLongClickListener() {

			@Override
			public boolean onLongClick(View v) {
				Toast.makeText(context, "toast " + m_List.get(pos), Toast.LENGTH_SHORT).show();
				return true;
			}
		});

		return convertView;
	}

	private class CustomHolder {
		TextView m_TextView;
		LinearLayout layout;
		View viewRight;
		View viewLeft;
		CircleImageView imageView;
	}

	static int img[] = {R.drawable.saimdang_spk, R.drawable.dabotap, R.drawable.sejong,
	R.drawable.hack_spk};
}

