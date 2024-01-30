package pie.ilikepiefoo.orevacuum.item;

import pie.ilikepiefoo.orevacuum.api.Shapes;
import pie.ilikepiefoo.orevacuum.api.VacuumLogic;
import pie.ilikepiefoo.orevacuum.api.vacuums.impl.DefaultBlockTraversalHandler;
import pie.ilikepiefoo.orevacuum.api.vacuums.impl.DefaultShapeProvider;

public class VacuumItemRectangleShaped extends VacuumItem {

    public VacuumItemRectangleShaped(Properties properties, int length, int width, int height, DefaultBlockTraversalHandler.Builder traversalHandlerBuilder) {
        super(properties, new VacuumLogic.Builder().setShapeProvider(new DefaultShapeProvider(Shapes.createRectangle(length, width, height))).setTraversalHandler(traversalHandlerBuilder.build()).build());
    }

}
