package toxTree.tree;

import java.awt.Dimension;
import java.io.OutputStream;

import javax.imageio.ImageIO;

import org.openscience.cdk.interfaces.IAtomContainer;

import toxTree.core.IDecisionMethod;
import toxTree.core.IDecisionRule;
import toxTree.core.IProcessRule;
import toxTree.exceptions.DecisionMethodIOException;
import toxTree.query.MolAnalyser;
import ambit2.rendering.CompoundImageTools;
import ambit2.rendering.IAtomContainerHighlights;

public abstract class AbstractTreePrinter implements IProcessRule {
	protected OutputStream outputStream = null;
    protected CompoundImageTools imageTools;

	public AbstractTreePrinter() {
		this(System.out);
	}
	public AbstractTreePrinter(OutputStream outputStream) {
		this.outputStream = outputStream;
		setOutputStream(outputStream);
        imageTools = new CompoundImageTools(new Dimension(150,150));
	}

	protected void setOutputStream(OutputStream outputStream) {
		this.outputStream = outputStream;
	}
	protected OutputStream getOutputStream() {
		return outputStream;
	}
	public void done() throws DecisionMethodIOException {
		if (outputStream != null) 
		try {
			outputStream.flush();
			outputStream.close();
		} catch (Exception x) {
			throw new DecisionMethodIOException(x);
		}
		
	}	
    protected void writeMolecule(IDecisionMethod tree,IDecisionRule rule, boolean answer, OutputStream out) throws Exception  {
        IAtomContainer m = rule.getExampleMolecule(answer);      
        MolAnalyser.analyse(m);
       
        ImageIO.write( tree.getImage(m, rule.getID(), 200,200,false), "png", out);
    }  
}
