package pie.ilikepiefoo.orevacuum.item;

import pie.ilikepiefoo.orevacuum.api.Shapes;
import pie.ilikepiefoo.orevacuum.api.VacuumLogic;
import pie.ilikepiefoo.orevacuum.api.vacuums.impl.DefaultBlockTraversalHandler;
import pie.ilikepiefoo.orevacuum.api.vacuums.impl.DefaultShapeProvider;

public class VacuumItemPyramidShaped extends VacuumItem {

    public VacuumItemPyramidShaped(Properties properties, int height, DefaultBlockTraversalHandler.Builder traversalHandlerBuilder) {
        super(properties, new VacuumLogic.Builder().setShapeProvider(new DefaultShapeProvider(Shapes.createPyramid(height))).setTraversalHandler(traversalHandlerBuilder.build()).build());
    }

}
