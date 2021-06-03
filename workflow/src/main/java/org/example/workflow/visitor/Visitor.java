package org.example.workflow.visitor;


import org.example.workflow.SharedContext;

public interface Visitor {

	void visit(SharedContext failed);
}
