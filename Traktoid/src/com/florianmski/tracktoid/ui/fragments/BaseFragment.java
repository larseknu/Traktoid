package com.florianmski.tracktoid.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragment;
import com.florianmski.tracktoid.StatusView;
import com.florianmski.tracktoid.db.DatabaseWrapper;

public abstract class BaseFragment extends SherlockFragment
{
	private StatusView sv;
	private boolean restoreStateCalled = false;
	private DatabaseWrapper dbw = null;
	
//	public static Fragment newInstanceTest(Context context, Bundle args)
//	{
//		return Fragment.instantiate(context, args.getString(TraktoidConstants.BUNDLE_CLASS), args);
//	}
	
//	public void launchActivityWithSingleFragment(Class<?> fragmentClass)
//	{
//		launchActivityWithSingleFragment(fragmentClass, null);
//	}
	
//	public void launchActivityWithSingleFragment(Class<?> fragmentClass, Bundle args)
//	{
//		Intent i = new Intent(getActivity(), SinglePaneActivity.class);
//		i.putExtra(TraktoidConstants.BUNDLE_CLASS, fragmentClass.getName());
//		if(args != null)
//			i.putExtras(args);
//		startActivity(i);
//	}
	
	public void launchActivity(Class<?> activityToLaunch, Bundle args)
	{
		Intent i = new Intent(getActivity(), activityToLaunch);
		if(args != null)
			i.putExtras(args);
		startActivity(i);
	}
	
	public void launchActivity(Class<?> activityToLaunch)
	{
		launchActivity(activityToLaunch, null);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		getActionBar().setHomeButtonEnabled(true);
		
		if(savedInstanceState != null)
		{
			onRestoreState(savedInstanceState);
			restoreStateCalled = true;
		}
	}
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) 
	{
		super.onActivityCreated(savedInstanceState);
		
		//in case the fragment use setRetainInstance(true)
		if(savedInstanceState != null && !restoreStateCalled)
		{
			onRestoreState(savedInstanceState);
			restoreStateCalled = true;
		}
	}
	
	@Override
	public void onSaveInstanceState(Bundle toSave)
	{
		super.onSaveInstanceState(toSave);
		onSaveState(toSave);
	}
	
	@Override
	public void onViewCreated(View v, Bundle savedInstanceState)
	{
		super.onViewCreated(v, savedInstanceState);
		
		sv = StatusView.instantiate(v);
	}
	
	public StatusView getStatusView()
	{
		return sv;
	}
	
	@Override
	public void onDestroy() 
	{
		super.onDestroy();
		if (dbw != null) 
		{
			dbw.close();
			dbw = null;
		}
	}

	protected DatabaseWrapper getDBWrapper() 
	{
		if (dbw == null)
			dbw = new DatabaseWrapper(getActivity());
		return dbw;
	}
	
	public ActionBar getActionBar()
	{
		return getSherlockActivity().getSupportActionBar();
	}
	
	protected void setTitle(String title)
	{
		getActionBar().setTitle(title);
	}

	protected void setSubtitle(String subtitle)
	{
		getActionBar().setSubtitle(subtitle);
	}
	
	public abstract void onRestoreState(Bundle savedInstanceState);
	public abstract void onSaveState(Bundle toSave);
}