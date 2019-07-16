package com.cai.smith.videogameapi.model.response;

import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

public class GameResponseList {

    @Field("items_per_page")
    private int itemsPerPage;

    @Field("start_index")
    private int startIndex;

    @Field("total_results")
    private int totalResults;

    @Field("items")
    private List<GameResponse> items;

    public int getItemsPerPage() {
        return itemsPerPage;
    }

    public void setItemsPerPage(int itemsPerPage) {
        this.itemsPerPage = itemsPerPage;
    }

    public int getStartIndex() {
        return startIndex;
    }

    public void setStartIndex(int startIndex) {
        this.startIndex = startIndex;
    }

    public int getTotalResults() {
        return totalResults;
    }

    public void setTotalResults(int totalResults) {
        this.totalResults = totalResults;
    }

    public List<GameResponse> getItems() {
        return items;
    }

    public void setItems(List<GameResponse> items) {
        this.items = items;
    }
}
