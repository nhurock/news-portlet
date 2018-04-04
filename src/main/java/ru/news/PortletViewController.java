package ru.news;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import ru.news.constant.NewsPortletConstant;
import ru.news.model.JournalArticleDTO;
import ru.news.service.JournalArticleDTOLocalServiceUtil;
import ru.news.service.LocalisationLocalServiceUtil;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

@Controller
@RequestMapping("VIEW")
public class PortletViewController {

    private static final String PAGE_VIEW = "newsblock-mvcportlet/view";
    private static final String PAGE_NEWS = "newsblock-mvcportlet/news";
    private static final String RENDER_MAPPING_ACTION_PARAM_RENDER_SINGLE_NEWS_PAGE = "action=" + NewsPortletConstant.METHOD_GET_ACTION_PARAM_VALUE_RENDER_SINGLE_NEWS_PAGE;

    @RenderMapping
    public String renderMainView(Model model) {
        return PAGE_VIEW;
    }

    @RenderMapping(params = RENDER_MAPPING_ACTION_PARAM_RENDER_SINGLE_NEWS_PAGE)
    public String renderSingleNewsView(RenderRequest request, RenderResponse response, Model model) {

        long groupId = Long.parseLong(request.getParameter(NewsPortletConstant.RENDER_REQUEST_PARAM_GROUP_ID));
        String article = request.getParameter(NewsPortletConstant.RENDER_REQUEST_PARAM_ARTICLE_ID);
        JournalArticleDTO journalArticleDTO = JournalArticleDTOLocalServiceUtil.getLatestVersion(groupId, article);

        LocalisationLocalServiceUtil.localize(journalArticleDTO, request.getLocale());

        model.addAttribute("news", journalArticleDTO);
        return PAGE_NEWS;
    }
}