package ru.news;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import ru.news.model.JournalArticleDTO;
import ru.news.service.JournalArticleCustomService;
import ru.news.service.LocalisationService;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import java.util.List;

@Controller
@RequestMapping("VIEW")
public class PortletViewController {

    private static final String PAGE_VIEW = "newsblock-mvcportlet/view";
    private static final String PAGE_NEWS = "newsblock-mvcportlet/news";
    private static final String RENDER_SINGLE_NEWS = "renderSingleNews";
    private static final String RENDER_TAG_VIEW = "renderTagView";
    private static final String RENDER_CATEGORY_VIEW = "renderCategoryView";
    private static final String ACTION_RENDER_SINGLE_NEWS = "action=" + RENDER_SINGLE_NEWS;
    private static final String ACTION_RENDER_TAG_VIEW = "action=" + RENDER_TAG_VIEW;
    private static final String ACTION_RENDER_CATEGORY_VIEW = "action=" + RENDER_CATEGORY_VIEW;

    private final JournalArticleCustomService journalArticleCustomService;

    @Autowired
    public PortletViewController(JournalArticleCustomService journalArticleCustomService) {
        this.journalArticleCustomService = journalArticleCustomService;
    }

    @RenderMapping
    public String renderMainView(Model model) {

        return PAGE_VIEW;
    }

    @RenderMapping(params = ACTION_RENDER_SINGLE_NEWS)
    public String renderSingleNewsView(RenderRequest request, RenderResponse response, Model model) {

        long groupId = Long.parseLong(request.getParameter("groupId"));
        String article = request.getParameter("articleId");
        JournalArticleDTO journalArticleDTO = journalArticleCustomService.getJournalArticleLatestVersion(groupId, article);

        LocalisationService.localize(journalArticleDTO, request.getLocale());

        model.addAttribute("news", journalArticleDTO);
        return PAGE_NEWS;
    }

    @RenderMapping(params = ACTION_RENDER_TAG_VIEW)
    public String renderTagView(RenderRequest request, RenderResponse response, Model model) {
        String tag = request.getParameter("tag");

        List<JournalArticleDTO> journalArticlesByTag = journalArticleCustomService.getJournalArticleByTag(tag);

        model.addAttribute("newsList", journalArticlesByTag);
        model.addAttribute("tag", tag);
        return PAGE_VIEW;
    }

    @RenderMapping(params = ACTION_RENDER_CATEGORY_VIEW)
    public String renderCategoryView(RenderRequest request, RenderResponse response, Model model) {
        String category = request.getParameter("category");

        List<JournalArticleDTO> journalArticlesByTag = journalArticleCustomService.getJournalArticleByCategory(category);

        model.addAttribute("newsList", journalArticlesByTag);
        model.addAttribute("category", category);
        return PAGE_VIEW;
    }

}