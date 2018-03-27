package ru.news.mapper;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portlet.asset.model.AssetCategory;
import com.liferay.portlet.asset.model.AssetEntry;
import com.liferay.portlet.asset.model.AssetTag;
import com.liferay.portlet.asset.service.AssetCategoryLocalServiceUtil;
import com.liferay.portlet.asset.service.AssetEntryLocalServiceUtil;
import com.liferay.portlet.asset.service.AssetTagLocalServiceUtil;
import com.liferay.portlet.journal.model.JournalArticle;
import ru.news.model.JournalArticleDTO;

import java.util.ArrayList;
import java.util.List;

public class JournalArticleMap {

    private static Log log = LogFactoryUtil.getLog(JournalArticleMap.class);

    public static JournalArticleDTO toDto(JournalArticle journalArticle) {
        if (journalArticle == null) return null;

        JournalArticleDTO journalArticleDTO = new JournalArticleDTO();

        journalArticleDTO.setGroupId(journalArticle.getGroupId());
        journalArticleDTO.setArticleId(journalArticle.getArticleId());

        journalArticleDTO.setTitle(journalArticle.getTitle());
        journalArticleDTO.setContent(journalArticle.getContent());

        journalArticleDTO.setPublishDate(journalArticle.getCreateDate());

        List<String> tags = new ArrayList<>();
        List<String> categories = new ArrayList<>();
        try {
            AssetEntry assetEntry = AssetEntryLocalServiceUtil.getEntry(journalArticle.getGroupId(), journalArticle.getArticleResourceUuid());
            List<AssetTag> assetEntryAssetTags = AssetTagLocalServiceUtil.getAssetEntryAssetTags(assetEntry.getEntryId());
            for (AssetTag assetTag : assetEntryAssetTags) {
                tags.add(assetTag.getName());
            }
            journalArticleDTO.setTags(tags);

            List<AssetCategory> assetCategories = AssetCategoryLocalServiceUtil.getCategories(JournalArticle.class.getName(), journalArticle.getResourcePrimKey());
            for (AssetCategory assetCategory : assetCategories) {
                categories.add(assetCategory.getName());
            }
            journalArticleDTO.setCategory(categories);
        } catch (SystemException | PortalException e) {
            log.trace(e);
        }
        return journalArticleDTO;
    }

    public static List<JournalArticleDTO> toDto(List<JournalArticle> journalArticles) {
        if (journalArticles == null) return null;

        List<JournalArticleDTO> journalArticleDTOS = new ArrayList<>();
        for (JournalArticle journalArticle : journalArticles) {
            journalArticleDTOS.add(toDto(journalArticle));
        }
        return journalArticleDTOS;
    }

}
