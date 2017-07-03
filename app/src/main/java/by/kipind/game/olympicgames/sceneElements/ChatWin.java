package by.kipind.game.olympicgames.sceneElements;

import android.content.Context;
import android.graphics.PointF;

import org.andengine.entity.Entity;
import org.andengine.entity.modifier.MoveYModifier;
import org.andengine.entity.modifier.ScaleAtModifier;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.adt.align.HorizontalAlign;
import org.andengine.util.adt.color.Color;

import java.util.ArrayList;
import java.util.List;

import by.kipind.game.olympicgames.GameSettings;
import by.kipind.game.olympicgames.R;
import by.kipind.game.olympicgames.ResourcesManager;

public class ChatWin extends Entity {
    private final Context context = GameSettings.getAppContext(); // context.getString(R.string.net_game_lb_total_id)
    protected final ResourcesManager resourcesManager = ResourcesManager.getInstance();
    protected VertexBufferObjectManager vbo;
    private final TextOptions tOpt = new TextOptions(HorizontalAlign.LEFT);

    protected List<Text> txtLines = new ArrayList<Text>();
    protected Text txtTitle;
    protected Text txtRecord;
    protected Text txtDivider;
    private Sprite chatBg;

    protected PointF startXY;
    protected int lineNum;

    private float recVal;
    private int curLastLine;

    // ===========================================================
    // ConstructorsIEntity entity,
    // ===========================================================
    public ChatWin(String recordTxt, final float pX, final float pY, final int lineNum, VertexBufferObjectManager vbo) {

	this.vbo = vbo;
	this.startXY = new PointF(pX + 5, pY);
	this.recVal = Float.valueOf(recordTxt);
	this.lineNum = lineNum;
	this.curLastLine = lineNum - 1;


	if (this.recVal < 0) {
	    this.recVal = 1000;
	    recordTxt = "----";
	}

	txtTitle = new Text(pX, pY, resourcesManager.font_pix_kir, context.getString(R.string.game_hud_per_progress), tOpt, vbo);
	txtTitle.setPosition(this.startXY.x, pY - txtTitle.getHeight() * 0.4f);
	txtTitle.setAnchorCenter(0, 0);
	txtTitle.setColor(Color.WHITE);
	txtTitle.setScale(0.45f);

	txtRecord = new Text(pX, pY, resourcesManager.font_pix_kir, "-0.1234567890", tOpt, vbo);
	txtRecord.setPosition(this.startXY.x, pY - (txtRecord.getHeight() * 0.4f + txtTitle.getHeight() * 0.5f));
	txtRecord.setAnchorCenter(0, 0);
	txtRecord.setText(recordTxt);
	txtRecord.setColor(Color.CYAN);
	txtRecord.setScale(0.5f);
	// txtTitle.setAnchorCenter(0, 0);

	txtDivider = new Text(this.startXY.x + 3, pY - (2 * txtRecord.getHeight() * 0.4f + txtTitle.getHeight() * 0.4f), resourcesManager.font_pix_kir, "<><><><><>", tOpt, vbo);
	txtDivider.setAnchorCenter(0, 0);
	txtDivider.setColor(Color.YELLOW);
	txtDivider.setScale(0.4f);
	// txtDivider.setAnchorCenter(0, 0);

	chatBg = new Sprite(pX, pY, resourcesManager.gameGraf.get("hud_element_chat_fon"), vbo);
	chatBg.setScale(1f, 1f);
	chatBg.setPosition(pX + chatBg.getWidth() / 2, pY - chatBg.getHeight() / 2);
	this.attachChild(chatBg);

	for (int i = 2; i < this.lineNum + 2; i++) {
	    txtLines.add(new Text(this.startXY.x, pY - i * txtRecord.getHeight() * 0.4f - 2 * txtTitle.getHeight() * 0.4f, resourcesManager.font_pix_kir, "-0.1234567890", tOpt,
		    vbo));
	    txtLines.get(i - 2).setText("----");
	    txtLines.get(i - 2).setAnchorCenter(0, 0);
	    txtLines.get(i - 2).setColor(0.8f, 0.99f, 0.8f);
	    txtLines.get(i - 2).setScale(0.4f);
	    this.attachChild(txtLines.get(i - 2));
	}

	this.attachChild(txtTitle);
	this.attachChild(txtRecord);
	this.attachChild(txtDivider);

    }

    // ===========================================================
    // Methods
    // ===========================================================

    public void addLine(String lineVal, boolean top) {
	if (top) {
	    txtRecord.setText(lineVal);
	}

    }

    public void addLine(float lineVal, boolean hiLow) {
	if ((this.recVal < lineVal) == hiLow || this.recVal == 1000) {
	    this.recVal = lineVal;
	    txtRecord.setText(String.valueOf(Math.abs(lineVal)));
	}

	txtLines.get(curLastLine).setVisible(false);
	txtLines.get(curLastLine).setPosition(txtLines.get(getNextLine(curLastLine)));

	int i = getPrivLine(curLastLine);
	while (i != curLastLine) {
	    txtLines.get(i).registerEntityModifier(new MoveYModifier(3, txtLines.get(i).getY(), txtLines.get(i).getY() - txtLines.get(i).getHeight() * 0.4f));
	    i = getPrivLine(i);
	}
	txtLines.get(i).setVisible(true);
	txtLines.get(i).setText(String.valueOf(Math.abs(lineVal)));
	txtLines.get(i).registerEntityModifier(new ScaleAtModifier(3, 0, 0.4f, 0, 0));

	// new MoveYModifier(3, txtLines.get(i).getY(), txtLines.get(i).getY() - txtLines.get(i).getHeight() * 0.4f));

	// txtTitle.registerEntityModifier(new MoveYModifier(3, txtTitle.getY(), txtTitle.getY() - txtTitle.getHeight() * 0.4f));

	curLastLine = getPrivLine(curLastLine);

    }

    private int getNextLine(int line) {
	if (line == this.lineNum - 1) {
	    return 0;
	} else {
	    return line + 1;
	}

    }

    private int getPrivLine(int line) {
	if (line == 0) {
	    return this.lineNum - 1;
	} else {
	    return line - 1;
	}

    }

    // ===========================================================
    // Getters & Setters
    // ===========================================================

    public Text getTxtTitle() {
	return txtRecord;
    }

    public void setTxtTitle(Text txtTitle) {
	this.txtRecord = txtTitle;
    }

    public PointF getStartXY() {
	return startXY;
    }

    public void setStartXY(PointF startXY) {
	this.startXY = startXY;
    }

    public void setStartXY(float x, float y) {
	this.startXY.x = x;
	this.startXY.y = y;
    }

    public int getLineNum() {
	return lineNum;
    }

    public void setLineNum(int lineNum) {
	this.lineNum = lineNum;
    }

    @Override
    public float getX() {
	return chatBg.getX();

    }

    @Override
    public float getY() {
	return chatBg.getY();

    }

    @Override
    public float getWidth() {
	return chatBg.getWidth();

    }

    @Override
    public float getHeight() {
	return chatBg.getHeight();

    }

}
