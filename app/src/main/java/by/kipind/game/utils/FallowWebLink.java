package by.kipind.game.utils;

import android.content.Intent;
import android.net.Uri;
import by.kipind.game.olympicgames.ResourcesManager;

public class FallowWebLink {

    public static void openWebLink(int linkId) {

	Uri data = null;

	switch (linkId) {
	case 1:
	    data = Uri.parse("http://goo.gl/sdG2Pn");

	    break;
	case 2:
	    data = Uri.parse("http://goo.gl/D8j5ZY");

	    break;
	case 3:
	    data = Uri.parse("http://goo.gl/Z3Q3T2");

	    break;

	default:
	    break;
	}
	if (!data.equals(null)) {
	    final Intent linkIntent = new Intent(Intent.ACTION_VIEW);
	    linkIntent.setData(data);
	    ResourcesManager.getInstance().activity.startActivity(linkIntent);

	}

    };

}
