package info.gratour.common.rest;

import info.gratour.common.db.*;
import info.gratour.common.error.ErrorWithCode;
import info.gratour.common.utils.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class QueryParamsResolver implements HandlerMethodArgumentResolver {

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(QueryParams.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest request, WebDataBinderFactory binderFactory) throws Exception {
        Map<String, String[]> map = request.getParameterMap();

        Integer limit = null;
        Integer page = null;
        Pagination pagination = null;
        SortColumn[] sortColumns = null;
        List<SearchCondition> searchConditions = new ArrayList<>();

        for (String key : map.keySet()) {
            String[] values = map.get(key);

            switch (key) {
                case "__limit":
                    limit = StringUtils.tryParseInt(values[0]);
                    if (limit == null)
                        throw ErrorWithCode.invalidParam("__limit");
                    break;

                case "__page":
                    page = StringUtils.tryParseInt(values[0]);
                    if (page == null)
                        throw ErrorWithCode.invalidParam("__page");
                    break;

                case "__sortBy":
                    sortColumns = QueryParams.parseSortColumns(values[0]);
                    break;

                default:
                    searchConditions.add(new SearchCondition(key, values[0]));
            }
        }

        if (limit != null) {
            if (page == null)
                throw ErrorWithCode.invalidParam("__page");

            pagination = new Pagination(limit, page);
        } else if (page != null) {
            throw ErrorWithCode.invalidParam("__limit");
        }

        return new QueryParams(SearchConditions.apply(searchConditions), pagination, sortColumns);
    }
}
