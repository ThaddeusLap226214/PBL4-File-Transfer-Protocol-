package Utils;

import java.io.File;

import javax.swing.JTree;
import javax.swing.event.TreeExpansionEvent;
import javax.swing.event.TreeWillExpandListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.ExpandVetoException;


public class FileTreePanel{
	private JTree tree;
	private DefaultMutableTreeNode root;
	
	public JTree tree() {
		return tree;
	}
	
	public FileTreePanel(String rootPath) {
		File fileRoot = new File(rootPath);
		root = new DefaultMutableTreeNode(fileRoot);
		tree = new JTree(root);
		
		addDummyNode(root);
		
		tree.addTreeWillExpandListener(new TreeWillExpandListener() {
			
			@Override
			public void treeWillExpand(TreeExpansionEvent event) throws ExpandVetoException {
				// TODO Auto-generated method stub
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) event.getPath().getLastPathComponent();
				loadChildren(node);
			}
			
			@Override
			public void treeWillCollapse(TreeExpansionEvent event) throws ExpandVetoException {
				// TODO Auto-generated method stub
				
			}
		});
		
	}
	
	//đánh dấu ... nếu thư mục có thể mở rộng
	private void addDummyNode(DefaultMutableTreeNode node) {
		File file = (File) node.getUserObject();
		if(file.isDirectory()) {
			node.add(new DefaultMutableTreeNode("..."));
		}
	}
	
	//Load thư mục con ghi sự kiện Expand mở thư mục
	private void loadChildren(DefaultMutableTreeNode node) {
		//bỏ qua node đã load
		if(node.getChildCount() == 1 && 
		node.getFirstChild().toString().equals("...")){
			node.removeAllChildren(); //xóa cái thư mục "..."
			
			File parentFile = (File) node.getUserObject();
			File[] files = parentFile.listFiles();
			
			if(files != null) {
				for(File f : files) {
					DefaultMutableTreeNode child = new DefaultMutableTreeNode(f);
					node.add(child);
					if(f.isDirectory()) addDummyNode(child); //thêm "..." nếu là thư mục
				}
			}
		}
		((DefaultTreeModel) tree.getModel()).reload(node);
	}
	
	private void addChildren(DefaultMutableTreeNode node) {
		File parentFile = (File) node.getUserObject();
		
		if(!parentFile.isDirectory()) return;
		
		if(node.getChildCount() > 0) return;
		
		File[] files = parentFile.listFiles();
		if(files != null) {
			for (File f : files) {
				node.add(new DefaultMutableTreeNode(f));
			}
		}
	}
	
	public static void main(String[] args) {
		
	}
}
