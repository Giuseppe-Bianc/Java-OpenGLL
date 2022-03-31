package bglengine;

import constant.Const;
import components.Sprite;
import components.SpriteRenderer;
import components.Spritesheet;
import org.joml.Vector2f;
import util.AssetPool;

public class LevelEditorScene extends Scene {

	private GameObject obj1;
	private Spritesheet sprites;

	public LevelEditorScene() {

	}

	@Override
	public void init() {
		loadResources();

		this.camera = new Camera(new Vector2f(-250, 0));

		sprites = AssetPool.getSpritesheet(Const.PRT + "spritesheet.png");

		obj1 = new GameObject("Object 1", new Transform(new Vector2f(200, 100),
				new Vector2f(256, 256)), 2);
		obj1.addComponent(new SpriteRenderer(new Sprite(
				AssetPool.getTexture(Const.BLN)
		)));
		this.addGameObjectToScene(obj1);

		GameObject obj2 = new GameObject("Object 2",
				new Transform(new Vector2f(400, 100), new Vector2f(256, 256)), 3);
		obj2.addComponent(new SpriteRenderer(new Sprite(
				AssetPool.getTexture(Const.BLN2)
		)));
		this.addGameObjectToScene(obj2);
	}

	private void loadResources() {
		AssetPool.getShader("assets/shaders/default.glsl");

		AssetPool.addSpritesheet(Const.PRT + "spritesheet.png",
				new Spritesheet(AssetPool.getTexture(Const.PRT + "spritesheet.png"),
						16, 16, 26, 0));
	}

	@Override
	public void update(float dt) {

		for (GameObject go : this.gameObjects) {
			go.update(dt);
		}

		this.renderer.render();
	}
}
