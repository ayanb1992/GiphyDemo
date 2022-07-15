package com.example.giphydemo.viewmodel

import com.example.giphydemo.util.Constants

class QueryParamsImpl : IQueryParams {

    private var queryMap: HashMap<String, String> = HashMap()

    override fun apiKey(apiKey: String): QueryParamsImpl {
        queryMap[Constants.ApiQueryParams.API_KEY] = apiKey
        return this
    }

    override fun limit(limit: Int): QueryParamsImpl {
        queryMap[Constants.ApiQueryParams.LIMIT] = limit.toString()
        return this
    }

    override fun rating(rating: String): QueryParamsImpl {
        queryMap[Constants.ApiQueryParams.RATING] = rating
        return this
    }

    override fun query(query: String): QueryParamsImpl {
        queryMap[Constants.ApiQueryParams.QUERY] = query
        return this
    }

    override fun offset(offset: Int): QueryParamsImpl {
        queryMap[Constants.ApiQueryParams.OFFSET] = offset.toString()
        return this
    }

    override fun lang(lang: String): QueryParamsImpl {
        queryMap[Constants.ApiQueryParams.LANG] = lang
        return this
    }

    override fun getQueryParams(): Map<String, String> {
        return queryMap
    }
}

object QueryFactory : IQueryCreator {
    override fun getTrendingGifsQuery(
        apiKey: String,
        limit: Int,
        rating: String
    ): Map<String, String> {
        return QueryParamsImpl()
            .apiKey(apiKey)
            .limit(limit)
            .rating(rating)
            .getQueryParams()
    }

    override fun getSearchQueryParams(
        apiKey: String,
        query: String,
        limit: Int,
        offset: Int,
        lang: String,
        rating: String
    ): Map<String, String> {
        return QueryParamsImpl()
            .apiKey(apiKey)
            .limit(limit)
            .query(query)
            .offset(offset)
            .lang(lang)
            .rating(rating)
            .getQueryParams()
    }
}

interface IQueryParams {
    fun apiKey(apiKey: String): QueryParamsImpl
    fun limit(limit: Int): QueryParamsImpl
    fun rating(rating: String): QueryParamsImpl
    fun query(query: String): QueryParamsImpl
    fun offset(offset: Int): QueryParamsImpl
    fun lang(lang: String): QueryParamsImpl
    fun getQueryParams(): Map<String, String>
}

interface IQueryCreator {
    fun getTrendingGifsQuery(apiKey: String, limit: Int, rating: String): Map<String, String>
    fun getSearchQueryParams(
        apiKey: String,
        query: String,
        limit: Int,
        offset: Int,
        lang: String,
        rating: String
    ): Map<String, String>
}