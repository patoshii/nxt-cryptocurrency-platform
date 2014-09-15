package nxt.http;

import nxt.Asset;
import nxt.NxtException;
import nxt.Trade;
import nxt.db.DbIterator;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.JSONStreamAware;

import javax.servlet.http.HttpServletRequest;

public final class GetTrades extends APIServlet.APIRequestHandler {

    static final GetTrades instance = new GetTrades();

    private GetTrades() {
        super(new APITag[] {APITag.AE}, "asset", "firstIndex", "lastIndex");
    }

    @Override
    JSONStreamAware processRequest(HttpServletRequest req) throws NxtException {

        Asset asset = ParameterParser.getAsset(req);
        int firstIndex = ParameterParser.getFirstIndex(req);
        int lastIndex = ParameterParser.getLastIndex(req);

        JSONObject response = new JSONObject();

        JSONArray tradesData = new JSONArray();
        try (DbIterator<Trade> trades = asset.getTrades(firstIndex, lastIndex)) {
            while (trades.hasNext()) {
                tradesData.add(JSONData.trade(trades.next()));
            }
        } catch (RuntimeException e) {
            response.put("error", e.toString());
        }
        response.put("trades", tradesData);

        return response;
    }

}
