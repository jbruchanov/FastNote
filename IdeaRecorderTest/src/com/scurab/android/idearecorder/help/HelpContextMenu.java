package com.scurab.android.idearecorder.help;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

public class HelpContextMenu implements ContextMenu
{

	@Override
	public MenuItem add(CharSequence title)
	{
		return null;
	}

	@Override
	public MenuItem add(int titleRes)
	{
		return null;
	}

	@Override
	public MenuItem add(int groupId, int itemId, int order, CharSequence title)
	{
		return null;
	}

	@Override
	public MenuItem add(int groupId, int itemId, int order, int titleRes)
	{
		return null;
	}

	@Override
	public SubMenu addSubMenu(CharSequence title)
	{
		return null;
	}

	@Override
	public SubMenu addSubMenu(int titleRes)
	{
		return null;
	}

	@Override
	public SubMenu addSubMenu(int groupId, int itemId, int order, CharSequence title)
	{
		return null;
	}

	@Override
	public SubMenu addSubMenu(int groupId, int itemId, int order, int titleRes)
	{
		return null;
	}

	@Override
	public int addIntentOptions(int groupId, int itemId, int order, ComponentName caller, Intent[] specifics, Intent intent, int flags,
			MenuItem[] outSpecificItems)
	{
		return 0;
	}

	@Override
	public void removeItem(int id)
	{
		
	}

	@Override
	public void removeGroup(int groupId)
	{

	}

	@Override
	public void clear()
	{

	}

	@Override
	public void setGroupCheckable(int group, boolean checkable, boolean exclusive)
	{

	}

	@Override
	public void setGroupVisible(int group, boolean visible)
	{

	}

	@Override
	public void setGroupEnabled(int group, boolean enabled)
	{

	}

	@Override
	public boolean hasVisibleItems()
	{
		return false;
	}

	@Override
	public MenuItem findItem(int id)
	{
		return null;
	}

	@Override
	public int size()
	{
		return 0;
	}

	@Override
	public MenuItem getItem(int index)
	{
		return null;
	}

	@Override
	public void close()
	{

	}

	@Override
	public boolean performShortcut(int keyCode, KeyEvent event, int flags)
	{
		return false;
	}

	@Override
	public boolean isShortcutKey(int keyCode, KeyEvent event)
	{
		return false;
	}

	@Override
	public boolean performIdentifierAction(int id, int flags)
	{
		return false;
	}

	@Override
	public void setQwertyMode(boolean isQwerty)
	{

	}

	@Override
	public ContextMenu setHeaderTitle(int titleRes)
	{
		return null;
	}

	@Override
	public ContextMenu setHeaderTitle(CharSequence title)
	{
		return null;
	}

	@Override
	public ContextMenu setHeaderIcon(int iconRes)
	{
		return null;
	}

	@Override
	public ContextMenu setHeaderIcon(Drawable icon)
	{
		return null;
	}

	@Override
	public ContextMenu setHeaderView(View view)
	{
		return null;
	}

	@Override
	public void clearHeader()
	{

	}

}
