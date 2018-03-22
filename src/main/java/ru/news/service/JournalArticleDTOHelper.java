package ru.news.service;

import com.liferay.portal.kernel.dao.orm.*;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.PortalClassLoaderUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portlet.asset.model.AssetCategory;
import com.liferay.portlet.asset.model.AssetEntry;
import com.liferay.portlet.asset.model.AssetTag;
import com.liferay.portlet.asset.service.AssetCategoryLocalServiceUtil;
import com.liferay.portlet.asset.service.AssetEntryLocalServiceUtil;
import com.liferay.portlet.asset.service.AssetTagLocalServiceUtil;
import com.liferay.portlet.journal.model.JournalArticle;
import ru.news.model.JournalArticleDTO;
import ru.news.search.JournalArticleDTODisplayTerms;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;


public class JournalArticleDTOHelper {

    private static final String PROPERTY_TITLE = "title";
    private static final String PROPERTY_RESOURCE_PRIM_KEY = "resourcePrimKey";
    private static final String PROPERTY_CONTENT = "content";
    private static final String PROPERTY_NAME = "name";

    public static List<JournalArticleDTO> getJournalArticle(JournalArticleDTODisplayTerms displayTerms, int start, int end) {
        List<JournalArticleDTO> articleDTOS = getJournalArticleData(displayTerms);
        if (articleDTOS == null) return null;

        return ListUtil.subList(articleDTOS, start, end);
    }

    /**
     * Возвращает количество записей
     */
    public static int getTotalJournalArticleCount(JournalArticleDTODisplayTerms displayTerms) {
        List<JournalArticleDTO> journalArticleData = getJournalArticleData(displayTerms);
        if (journalArticleData == null) return 0;
        return journalArticleData.size();
    }

    private static List<JournalArticleDTO> getJournalArticleData(JournalArticleDTODisplayTerms displayTerms) {
        List<JournalArticleDTO> journalArticles;
        Locale locale = displayTerms.getLocale();
        ClassLoader classLoader = PortalClassLoaderUtil.getClassLoader();
        if (Validator.isBlank(displayTerms.getKeywords()) && (!displayTerms.isAdvancedSearch())) {
//            Получения данных без фильтров поиска
            journalArticles = JournalArticleDTOLocalServiceUtil.getApprovedAndExpiredJournalArticlesLatestVersion();
        } else {

            DynamicQuery dynamicQueryJournalArticle = DynamicQueryFactoryUtil.forClass(JournalArticle.class, "journalArticle", classLoader);
            Junction junctionJournalArticle;
//            Расширенный поиск
            if (displayTerms.isAdvancedSearch()) {
                if (displayTerms.isAndOperator()) {
                    junctionJournalArticle = RestrictionsFactoryUtil.conjunction();
                } else {
                    junctionJournalArticle = RestrictionsFactoryUtil.disjunction();
                }

                if (!Validator.isBlank(displayTerms.getTitle())) {
                    junctionJournalArticle.add(PropertyFactoryUtil.forName(PROPERTY_TITLE).like("%" + displayTerms.getTitle() + "%"));
                }
                String tagName = displayTerms.getTag();
                if (!Validator.isBlank(tagName)) {
                    Junction disjunction = RestrictionsFactoryUtil.disjunction();
                    for (Long resourcePrimaryKey : getJournalArticleResourcePrimKeyByTag(tagName)) {
                        disjunction.add(PropertyFactoryUtil.forName(PROPERTY_RESOURCE_PRIM_KEY).eq(resourcePrimaryKey));
                    }
                    junctionJournalArticle.add(disjunction);
                }
                String categoryName = displayTerms.getCategory();
                if (!Validator.isBlank(categoryName)) {
                    Junction disjunction = RestrictionsFactoryUtil.disjunction();
                    for (Long resourcePrimKey : getJournalArticleResourcePrimKeyByCategories(categoryName)) {
                        disjunction.add(PropertyFactoryUtil.forName(PROPERTY_RESOURCE_PRIM_KEY).eq(resourcePrimKey));
                    }
                    junctionJournalArticle.add(disjunction);
                }

            } else {
//                 Поиск по основному полю
                junctionJournalArticle = RestrictionsFactoryUtil.disjunction();
                junctionJournalArticle.add(PropertyFactoryUtil.forName(PROPERTY_TITLE).like("%" + displayTerms.getKeywords() + "%"));
                junctionJournalArticle.add(PropertyFactoryUtil.forName(PROPERTY_CONTENT).like("%" + displayTerms.getKeywords() + "%"));
            }
            dynamicQueryJournalArticle.add(junctionJournalArticle);
            journalArticles = JournalArticleDTOLocalServiceUtil.getDynamicQuery(dynamicQueryJournalArticle);
        }

//         Фильтрация контента по контенту
        if (!displayTerms.getEnableArchiveNews()) {
            // Удаление архивных новостей
            JournalArticleDTOLocalServiceUtil.deleteExpiredJournalArticle(journalArticles);
        }

//         Локализация контента
        LocalisationService.localize(journalArticles, locale);
        return journalArticles;
    }

    /**
     * Возвращает список resourcePrimKey сущностей JournalArticle по заданной категории
     *
     * @param categoriesName имя категории
     */
    private static List<Long> getJournalArticleResourcePrimKeyByCategories(String categoriesName) {
        ClassLoader classLoader = PortalClassLoaderUtil.getClassLoader();
        DynamicQuery dynamicQueryAssetCategories = DynamicQueryFactoryUtil.forClass(AssetCategory.class, "assetCategories", classLoader);
        Junction junctionAssetCategories = RestrictionsFactoryUtil.disjunction();
        junctionAssetCategories.add(PropertyFactoryUtil.forName(PROPERTY_NAME).like(categoriesName));
        dynamicQueryAssetCategories.add(junctionAssetCategories);
        List<AssetCategory> assetCategories = new ArrayList<>();
        List<Long> resourcePrimKeyList = new ArrayList<>();
        try {
            assetCategories = AssetCategoryLocalServiceUtil.dynamicQuery(dynamicQueryAssetCategories);
        } catch (SystemException e) {
            e.printStackTrace();
        }
        if (assetCategories != null) {
            for (AssetCategory assetCategory : assetCategories) {
                try {
                    List<AssetEntry> assetEntryAssetCategories = AssetEntryLocalServiceUtil.getAssetCategoryAssetEntries(assetCategory.getCategoryId());
                    long resourcePrimaryKey;
                    for (AssetEntry assetEntry : assetEntryAssetCategories) {
                        resourcePrimaryKey = assetEntry.getClassPK();
                        resourcePrimKeyList.add(resourcePrimaryKey);

                    }
                } catch (SystemException e) {
                    e.printStackTrace();
                }
            }
        }
        return resourcePrimKeyList;
    }

    /**
     * Возвращает список resourcePrimKey сущностей JournalArticle по задданому тэгу
     *
     * @param tagName имя тэга новости
     */
    private static List<Long> getJournalArticleResourcePrimKeyByTag(String tagName) {
        ClassLoader classLoader = PortalClassLoaderUtil.getClassLoader();
        DynamicQuery dynamicQueryAssetTag = DynamicQueryFactoryUtil.forClass(AssetTag.class, "assetTag", classLoader);
        Junction junctionAssetTag = RestrictionsFactoryUtil.disjunction();
        junctionAssetTag.add(PropertyFactoryUtil.forName(PROPERTY_NAME).like(tagName));
        dynamicQueryAssetTag.add(junctionAssetTag);
        List<AssetTag> assetTags = new ArrayList<>();
        List<Long> resourcePrimKeyList = new ArrayList<>();
        try {
            assetTags = AssetTagLocalServiceUtil.dynamicQuery(dynamicQueryAssetTag);
        } catch (SystemException e) {
            e.printStackTrace();
        }
        if (assetTags != null) {
            for (AssetTag assetTag : assetTags) {
                try {
                    List<AssetEntry> assetTagAssetEntries = AssetEntryLocalServiceUtil.getAssetTagAssetEntries(assetTag.getTagId());
                    long resourcePrimaryKey;
                    for (AssetEntry assetEntry : assetTagAssetEntries) {
                        resourcePrimaryKey = assetEntry.getClassPK();
                        resourcePrimKeyList.add(resourcePrimaryKey);
                    }
                } catch (SystemException e) {
                    e.printStackTrace();
                }
            }
        }
        return resourcePrimKeyList;
    }

}
