package ru.news;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import ru.news.model.JournalArticleDTO;
import ru.news.service.JournalArticleDTOLocalServiceUtil;
import ru.news.service.LocalisationService;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

@Controller
@RequestMapping("VIEW")
public class PortletViewController {

    private static final String PAGE_VIEW = "newsblock-mvcportlet/view";
    private static final String PAGE_NEWS = "newsblock-mvcportlet/news";
    private static final String RENDER_SINGLE_NEWS = "renderSingleNews";
    private static final String ACTION_RENDER_SINGLE_NEWS = "action=" + RENDER_SINGLE_NEWS;

    @RenderMapping
    public String renderMainView(Model model) {

        return PAGE_VIEW;
    }

    @RenderMapping(params = ACTION_RENDER_SINGLE_NEWS)
    public String renderSingleNewsView(RenderRequest request, RenderResponse response, Model model) {

        long groupId = Long.parseLong(request.getParameter("groupId"));
        String article = request.getParameter("articleId");
        JournalArticleDTO journalArticleDTO = JournalArticleDTOLocalServiceUtil.getJournalArticleLatestVersion(groupId, article);

        LocalisationService.localize(journalArticleDTO, request.getLocale());

        model.addAttribute("news", journalArticleDTO);
        return PAGE_NEWS;
    }
}