package by.kipind.game.olympicgames.sceneElements;

import android.content.Intent;
import android.net.Uri;

import org.andengine.engine.handler.timer.ITimerCallback;
import org.andengine.engine.handler.timer.TimerHandler;
import org.andengine.entity.Entity;
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

public class RatePanel extends Entity {
    // ===========================================================
    // Constants
    // ===========================================================
    private final TextOptions tOpt = new TextOptions(HorizontalAlign.LEFT);

    // ===========================================================

    protected final ResourcesManager resourcesManager = ResourcesManager.getInstance();
    protected VertexBufferObjectManager vbo;

    private Text mRateLabel;
    private Sprite fon;
    private Sprite mail;
    private List<Sprite> starsStack = new ArrayList<Sprite>();

    protected int objTotal, objNow;

    private int timePas = 1, state = 1;

    // ===========================================================
    // ConstructorsIEntity entity,
    // ===========================================================
    public RatePanel(final float pX, final float pY, VertexBufferObjectManager vbo) {

	this.vbo = vbo;
	this.objTotal = 5;
	this.objNow = 0;

	fon = new Sprite(pX, pY, resourcesManager.rate_fon, vbo);
	fon.setScale(1, 0.85f);
	this.attachChild(fon);

	mail = new Sprite(pX, pY, resourcesManager.rate_mail, vbo);
	mail.setPosition(pX - this.getWidth() / 3, pY);
	mail.setScale(0.9f);
	mail.setVisible(false);
	this.attachChild(mail);
	//
	mRateLabel = new Text(0, 0, resourcesManager.font_pix_kir, "Rate game, please. Пожалуйста оцените игру.", tOpt, vbo);// "<><><><><><><><><><><><>"
	mRateLabel.setColor(Color.CYAN);
	mRateLabel.setScale(0.4f);
	mRateLabel.setAnchorCenter(0, 0);
	mRateLabel.setPosition(fon.getX() - this.getWidth() * 0.5f, fon.getY() + this.getHeight() * 0.4f);
	mRateLabel.setText(GameSettings.getAppContext().getString(R.string.rate_panel_lb));
	this.attachChild(mRateLabel);

	float startXForDarts = fon.getX() - this.getWidth() * 0.35f;

	starsStack.add(new Sprite(startXForDarts, fon.getY(), resourcesManager.rate_black_star, vbo));
	starsStack.add(new Sprite(startXForDarts, fon.getY(), resourcesManager.rate_gold_star, vbo));
	this.attachChild(starsStack.get(0));
	this.attachChild(starsStack.get(1));

	float blStarHeight = starsStack.get(0).getHeight();

	for (int i = 1; i < objTotal; i++) {
	    starsStack.add(new Sprite(startXForDarts + i * blStarHeight, fon.getY(), resourcesManager.rate_black_star, vbo));
	    starsStack.add(new Sprite(startXForDarts + i * blStarHeight, fon.getY(), resourcesManager.rate_gold_star, vbo));

	    this.attachChild(starsStack.get(2 * i));
	    this.attachChild(starsStack.get(2 * i + 1));
	}

	this.registerUpdateHandler(new TimerHandler(0.2f, true, new ITimerCallback() {

	    @Override
	    public void onTimePassed(final TimerHandler pTimerHandler) {
		if (state == 1) {
		    if (timePas <= 10) {
			if (timePas % 2 == 0) {
			    plusObj();
			}
		    } else if (timePas > 10 && timePas <= 17) {
			shine();
		    } else if (timePas > 50) {
			timePas = 0;
			reSet();
		    }
		    timePas++;
		}
	    }
	}));

    }

    // ===========================================================
    // Methods
    // ===========================================================

    protected void shine() {
	for (int i = 0; i < objTotal; i++) {
	    starsStack.get(2 * i + 1).setVisible(!starsStack.get(2 * i + 1).isVisible());
	}
    }

    public int minusObj() {
	if (objNow > 0) {
	    starsStack.get(2 * objNow - 1).setVisible(false);
	    objNow--;
	}
	return objNow;
    }

    public int plusObj() {
	if (objNow < objTotal) {
	    starsStack.get(2 * objNow + 1).setVisible(true);
	    objNow++;
	}
	return objNow;
    }

    public void reSet() {
	for (int i = 0; i < objTotal; i++) {
	    starsStack.get(2 * i + 1).setVisible(false);
	}
	objNow = 0;
    }

    public void changeToInviteMail() {
	this.state = 2;
	for (int i = 0; i < starsStack.size(); i++) {
	    starsStack.get(i).setVisible(false);
	}
	mail.setVisible(true);

	// mRateLabel.setScale(0.4f);
	mRateLabel.setPosition(mail.getX() + mail.getWidth() / 2, fon.getY() - mRateLabel.getHeight() * 0.4f);
	mRateLabel.setText(GameSettings.getAppContext().getString(R.string.rate_panel_lb2));

    }

    public void gotoGP() {
	Intent goToMarket = null;
	goToMarket = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=by.kipind.game.olympicgames"));
	ResourcesManager.getInstance().activity.startActivity(goToMarket);
	GameSettings.rateAction(1);
    }

    public void gotoMail() {
	final Intent emailIntent = new Intent(Intent.ACTION_VIEW);
	Uri data = Uri
		.parse("mailto:" + GameSettings.getAppContext().getString(R.string.rate_panel_contact_mail) + "?subject="
			+ GameSettings.getAppContext().getString(R.string.rate_panel_contact_subject) + "&body="
			+ GameSettings.getAppContext().getString(R.string.rate_panel_contact_body));
	emailIntent.setData(data);
	ResourcesManager.getInstance().activity.startActivity(emailIntent);

	/* emailIntent.setType("message/rfc822"); emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] { "kudziev.ip@gmail.com" }); emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "subject");
	 * emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, "body text"); ResourcesManager.getInstance().activity.startActivity(Intent.createChooser(emailIntent, "Send mail...")); */GameSettings
		.rateAction(2);
    }

    public void gotoInviteMail() {
	final Intent emailIntent = new Intent(Intent.ACTION_VIEW);
	Uri data = Uri
.parse("mailto:?subject=" + GameSettings.getAppContext().getString(R.string.rate_panel_invite_subject) + "&body="
		+ GameSettings.getAppContext().getString(R.string.rate_panel_invite_body));
	emailIntent.setData(data);
	ResourcesManager.getInstance().activity.startActivity(emailIntent);


    }

    /* public float getWight() { return this.mWight; } */
    // ===========================================================
    // Getters & Setters
    // ===========================================================
    @Override
    public float getWidth() {
	return this.fon.getWidth();
    }

    @Override
    public float getHeight() {
	return this.fon.getHeight() * 0.8f + this.mRateLabel.getHeight() * 0.4f;
    }

    public int getObjNow() {
	return objNow;
    }

    public int getState() {
	return state;
    }


}