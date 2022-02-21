//import components.Transform;
//import ecs.Entity;
//import ecs.scripts.Script;
//import logging.Logger;
//
//public class TestScript extends Script {
//    double totalTime = 0;
//
//    @Override
//    public void onUpdate(double deltaTime) {
//
//        totalTime += deltaTime;
//
//        Entity entity = getEntity();
//
//        Transform transform = entity.getComponent(Transform.class);
//
//        // Occelate the scale
//        double scale = Math.sin(totalTime) * 100 + 100;
//
//        transform.setScale(scale, scale);
//    }
//}
