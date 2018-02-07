package ru.news;

import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.ReleaseInfo;
import com.liferay.portlet.journal.service.JournalArticleLocalServiceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.bind.annotation.RenderMapping;
import ru.news.model.JournalArticleDTO;
import ru.news.service.JournalArticleService;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("VIEW")
public class PortletViewController {

    private final
    JournalArticleService journalArticleService;

    @Autowired
    public PortletViewController(JournalArticleService journalArticleService) {
        this.journalArticleService = journalArticleService;
    }

    @RenderMapping
    public String question(Model model) {
        model.addAttribute("releaseInfo", ReleaseInfo.getBuildDate());

        List<JournalArticleDTO> articleDTOS = new ArrayList<>();

        try {
            if (JournalArticleLocalServiceUtil.getJournalArticlesCount() > 0) {

                articleDTOS = journalArticleService.getJournalArticlesLatestVersion();

            }
        } catch (SystemException e) {
            e.printStackTrace();
        }

        model.addAttribute("newsList", articleDTOS);

        return "newsblock-mvcportlet/view";
    }

    @RenderMapping(params = "action=renderOne")
    public String renderOneMethod(RenderRequest request, RenderResponse response, Model model) {

        long groupId = Long.parseLong(request.getParameter("groupId"));
        String article = request.getParameter("articleId");
        JournalArticleDTO journalArticleDTO = journalArticleService.getJournalArticleLatestVersion(groupId, article);

        model.addAttribute("news", journalArticleDTO);
        return "newsblock-mvcportlet/news";
    }

}