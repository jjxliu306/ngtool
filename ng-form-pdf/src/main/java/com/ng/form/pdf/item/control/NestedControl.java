package com.ng.form.pdf.item.control;

import java.util.List;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Element;
import com.itextpdf.text.ElementListener;

/**
 * 弹性容器组件
 * @author lyf
 *
 */
public class NestedControl implements Element{

	@Override
	public boolean process(ElementListener listener) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int type() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isContent() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isNestable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public List<Chunk> getChunks() {
		// TODO Auto-generated method stub
		return null;
	}
 

}
