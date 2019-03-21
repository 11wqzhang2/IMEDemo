package com.example.test006;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import android.content.Context;
import android.inputmethodservice.InputMethodService;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * @author chengang
 * @time 2015��5��15�� ����2:24:34
 */
public class helloIme extends InputMethodService {

	private List<String> suggestionlist; // ��ǰ��ѡ�ʱ�
	private Hashtable<String, List<String>> data; // �ʵ�����
	private KeyboardView mkeyView;
	private CandidateView mCandView;

	// InputMethodService������ʱ��ϵͳ����ø÷��������������»��ٱ�
	public void onInitializeInterface() { 
		// ��ʼ�� �ʵ�����
		data = new Hashtable<String, List<String>>();
		List<String> list = new ArrayList<String>();
		list.add("if(){}");
		list.add("if(){}else if(){}");
		list.add("if(){}else{}");
		data.put("if", list);

		list = new ArrayList<String>();
		list.add("while(){}");
		data.put("while", list);
	}

	public View onCreateInputView() {
		mkeyView = new KeyboardView(this);
		return mkeyView;
	}

	public View onCreateCandidatesView() {
		mCandView = new CandidateView(this);
		mCandView.setService(this);
		return mCandView;
	}

	public void pickSuggestionManually(int mSelectedIndex) {
		getCurrentInputConnection().commitText(
				suggestionlist.get(mSelectedIndex), 0); // ��������������
		setCandidatesViewShown(false); // ���� CandidatesView
	}

	public void onKey(CharSequence text) {
		// ���ݰ��µİ�ť���ú�ѡ���б�
		suggestionlist = data.get(text);
		mCandView.setSuggestions(suggestionlist);
	}

	class CandidateView extends RelativeLayout {
		TextView tv; // �м���ʾ��ѡ��
		Button btLeft, btRight; // ���Ұ�ť
		helloIme listener; // helloIme ���ڷ���ѡ�е� ��ѡ���±�
		// ��ѡ���б� KeyboardView ��ͬ�ļ����º��������ص��б�
		List<String> suggestions; 
		int mSelectedIndex = -1; // ��ǰ ��ѡ���±�

		public CandidateView(Context context) {
			super(context);

			tv = new TextView(context);
			tv.setId(1);
			RelativeLayout.LayoutParams lpCenter = new RelativeLayout.LayoutParams(
					200, ViewGroup.LayoutParams.WRAP_CONTENT);
			lpCenter.addRule(RelativeLayout.CENTER_IN_PARENT);
			addView(tv, lpCenter);
			tv.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					listener.pickSuggestionManually(mSelectedIndex);
				}
			});

			btLeft = new Button(context);
			btLeft.setText("<");
			btLeft.setOnClickListener(new OnClickListener() {
				public void onClick(View arg0) {
					mSelectedIndex = mSelectedIndex > 0 ? (mSelectedIndex - 1)
							: 0;
					tv.setText(suggestions.get(mSelectedIndex));
				}
			});

			RelativeLayout.LayoutParams lpLeft = new RelativeLayout.LayoutParams(
					60, ViewGroup.LayoutParams.WRAP_CONTENT);
			lpLeft.addRule(RelativeLayout.LEFT_OF, 1);
			addView(btLeft, lpLeft);

			btRight = new Button(context);
			btRight.setText(">");
			btRight.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					mSelectedIndex = mSelectedIndex >= suggestions.size() - 1 ? suggestions
							.size() - 1 : mSelectedIndex + 1;
					tv.setText(suggestions.get(mSelectedIndex));
				}
			});

			RelativeLayout.LayoutParams lpRight = new RelativeLayout.LayoutParams(
					60, ViewGroup.LayoutParams.WRAP_CONTENT);
			lpRight.addRule(RelativeLayout.RIGHT_OF, 1);
			addView(btRight, lpRight);
		}

		public void setService(helloIme listener) {
			this.listener = listener;
		}

		public void setSuggestions(List<String> suggestions) {
			mSelectedIndex = 0;
			tv.setText(suggestions.get(mSelectedIndex));
			this.suggestions = suggestions;
		}
	}

	class KeyboardView extends RelativeLayout {
		public KeyboardView(Context context) {
			super(context);

			Button btIf = new Button(context);
			btIf.setText("if");
			btIf.setId(1);
			RelativeLayout.LayoutParams lpIf = new RelativeLayout.LayoutParams(
					100, 50);
			lpIf.addRule(RelativeLayout.CENTER_HORIZONTAL);

			btIf.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					setCandidatesViewShown(true); // ��ʾ CandidateView
					helloIme.this.onKey("if"); // �������ť��ֵ���ظ� InputMethodService
				}
			});
			addView(btIf, lpIf);

			Button btWhile = new Button(context);
			btWhile.setText("while");
			RelativeLayout.LayoutParams lpWhile = new RelativeLayout.LayoutParams(
					100, 50);
			lpWhile.addRule(RelativeLayout.BELOW, 1);
			lpWhile.addRule(RelativeLayout.ALIGN_LEFT, 1);

			btWhile.setOnClickListener(new OnClickListener() {
				public void onClick(View v) {
					setCandidatesViewShown(true);
					helloIme.this.onKey("while");
				}
			});
			addView(btWhile, lpWhile);
		}
	}
}
