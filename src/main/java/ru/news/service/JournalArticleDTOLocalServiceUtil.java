package ru.news.service;

import com.liferay.portal.kernel.dao.orm.*;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ListUtil;
import com.liferay.portal.kernel.util.PortalClassLoaderUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.workflow.WorkflowConstants;
import com.liferay.portlet.asset.model.AssetCategory;
import com.liferay.portlet.asset.model.AssetEntry;
import com.liferay.portlet.asset.model.AssetTag;
import com.liferay.portlet.asset.service.AssetCategoryLocalServiceUtil;
import com.liferay.portlet.asset.service.AssetEntryLocalServiceUtil;
import com.liferay.portlet.asset.service.AssetTagLocalServiceUtil;
import com.liferay.portlet.journal.model.JournalArticle;
import com.liferay.portlet.journal.service.JournalArticleLocalServiceUtil;
import ru.news.mapper.JournalArticleMap;
import ru.news.model.JournalArticleDTO;
import ru.news.search.JournalArticleDTODisplayTerms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class JournalArticleDTOLocalServiceUtil {

    private static final String PROPERTY_TITLE = "title";
    private static final String PROPERTY_RESOURCE_PRIM_KEY = "resourcePrimKey";
    private static final String PROPERTY_CONTENT = "content";
    private static final String PROPERTY_NAME = "name";
    private static final String PROPERTY_STATUS = "status";
    private static Log log = LogFactoryUtil.getLog(JournalArticleDTOLocalServiceUtil.class);

    /**
     * Возвращает последнюю версию WebContent {@link JournalArticleDTO}
     *
     * @param articleId ID {@link JournalArticle}
     * @param groupId   groupId {@link JournalArticle}
     */
    public static JournalArticleDTO getLatestVersion(long groupId, String articleId) {
        if ((groupId == 0) || (articleId == null)) {
            throw new IllegalArgumentException("Can't get latest version journal article by groupId " + groupId + " and articleId " + articleId);
        }
        JournalArticle journalArticle = null;
        try {
            journalArticle = JournalArticleLocalServiceUtil.getLatestArticle(groupId, articleId);
        } catch (PortalException | SystemException e) {
            log.info("Can't get JournalArticles last version by groupId " + groupId + " and articleId " + articleId + ".");
            log.error(e);
        }
        return JournalArticleMap.toDto(journalArticle);
    }

    /**
     * Возвращает новости из запроса {@link DynamicQuery}
     *
     * @param dynamicQuery для поиска {@link JournalArticle}
     */
    private static List<JournalArticleDTO> getDynamicQuery(DynamicQuery dynamicQuery) {
        if (dynamicQuery == null) {
            throw new IllegalArgumentException("Empty DynamicQuery.");
        }
        List<JournalArticle> dynamicQuery1 = null;
        try {
            dynamicQuery1 = JournalArticleLocalServiceUtil.dynamicQuery(dynamicQuery);
        } catch (SystemException e) {
            log.info("Can't get DynamicQuery from JournalArticleLocalServiceUtil.");
            log.error(e);
        }

        if (dynamicQuery1 == null) {
            throw new IllegalArgumentException("Can't get List<JournalArticle> from null DynamicQuery.");
        }

        HashMap<String, JournalArticleDTO> journalArticleHashMap = new HashMap<>();
        for (JournalArticle journalArticle : dynamicQuery1) {
            String articleId = journalArticle.getArticleId();
            if (!journalArticleHashMap.containsKey(articleId)) {
                journalArticleHashMap.put(articleId, getLatestVersion(journalArticle.getGroupId(), journalArticle.getArticleId()));
            }
        }
        return new ArrayList<>(journalArticleHashMap.values());
    }

    /**
     * Возвращает список новостей из поиска, список фиксированного размера
     *
     * @param displayTerms параметры запроса
     * @param start        номер первой записи
     * @param end          номер последней записи
     */
    public static List<JournalArticleDTO> getJournalArticles(JournalArticleDTODisplayTerms displayTerms, int start, int end) {
        if (displayTerms == null) {
            throw new IllegalArgumentException("Can't get JournalArticle with null JournalArticleDTODisplayTerms.");
        }
        List<JournalArticleDTO> articleDTOS = getJournalArticleData(displayTerms);
        if (articleDTOS == null) {
            throw new IllegalArgumentException("Haven't JournalArticle's data from search.");
        }
        return ListUtil.subList(articleDTOS, start, end);
    }

    /**
     * Возвращает количество записей
     */
    public static int getTotalJournalArticleCount(JournalArticleDTODisplayTerms displayTerms) {
        if (displayTerms == null) {
            throw new IllegalArgumentException("Can't get data's count with null JournalArticleDTODisplayTerms.");
        }
        List<JournalArticleDTO> journalArticleData = getJournalArticleData(displayTerms);
        if (journalArticleData == null) return 0;
        return journalArticleData.size();
    }

    /**
     * Возращает список новстей в соответсвии с формой запроса
     *
     * @param displayTerms параметры поиска
     */
    private static List<JournalArticleDTO> getJournalArticleData(JournalArticleDTODisplayTerms displayTerms) {
        if (displayTerms == null) {
            throw new IllegalArgumentException("Can't get List<JournalArticle> with null JournalArticleDTODisplayTerms.");
        }
        List<JournalArticleDTO> journalArticles;
        Locale locale = displayTerms.getLocale();
        ClassLoader classLoader = PortalClassLoaderUtil.getClassLoader();
        DynamicQuery dynamicQueryJournalArticle = DynamicQueryFactoryUtil.forClass(JournalArticle.class, "journalArticle", classLoader);
        Junction junctionJournalArticle;
        if (Validator.isBlank(displayTerms.getKeywords()) && (!displayTerms.isAdvancedSearch())) {
//            Получения данных без фильтров поиска
            junctionJournalArticle = RestrictionsFactoryUtil.disjunction();
            if (displayTerms.getEnableArchiveNews()) {
                junctionJournalArticle.add(PropertyFactoryUtil.forName(PROPERTY_STATUS).eq(WorkflowConstants.STATUS_EXPIRED));
            }
            junctionJournalArticle.add(PropertyFactoryUtil.forName(PROPERTY_STATUS).eq(WorkflowConstants.STATUS_APPROVED));
            dynamicQueryJournalArticle.add(junctionJournalArticle);
            journalArticles = JournalArticleDTOLocalServiceUtil.getDynamicQuery(dynamicQueryJournalArticle);
        } else {
//            Расширенный поиск
            if (displayTerms.isAdvancedSearch()) {
                if (displayTerms.isAndOperator()) {
                    junctionJournalArticle = RestrictionsFactoryUtil.conjunction();
                } else {
                    junctionJournalArticle = RestrictionsFactoryUtil.disjunction();
                }

                if (!Validator.isBlank(displayTerms.getTitle())) {
                    junctionJournalArticle.add(RestrictionsFactoryUtil.ilike(PROPERTY_TITLE, "%" + displayTerms.getKeywords() + "%"));
                }
                String tagName = displayTerms.getTag();
                if (!Validator.isBlank(tagName)) {
                    Junction disjunction = RestrictionsFactoryUtil.disjunction();
                    for (Long resourcePrimaryKey : getJournalArticlesResourcePrimKeysByTag(tagName)) {
                        disjunction.add(PropertyFactoryUtil.forName(PROPERTY_RESOURCE_PRIM_KEY).eq(resourcePrimaryKey));
                    }
                    junctionJournalArticle.add(disjunction);
                }
                String categoryName = displayTerms.getCategory();
                if (!Validator.isBlank(categoryName)) {
                    Junction disjunction = RestrictionsFactoryUtil.disjunction();
                    for (Long resourcePrimKey : getJournalArticlesResourcePrimKeysByCategories(categoryName)) {
                        disjunction.add(PropertyFactoryUtil.forName(PROPERTY_RESOURCE_PRIM_KEY).eq(resourcePrimKey));
                    }
                    junctionJournalArticle.add(disjunction);
                }

            } else {
//                 Поиск по основному полю
                junctionJournalArticle = RestrictionsFactoryUtil.conjunction();
                Junction disjunction = RestrictionsFactoryUtil.disjunction();
                disjunction.add(RestrictionsFactoryUtil.ilike(PROPERTY_TITLE, "%" + displayTerms.getKeywords() + "%"));
                disjunction.add(RestrictionsFactoryUtil.ilike(PROPERTY_CONTENT, "%" + displayTerms.getKeywords() + "%"));
                junctionJournalArticle.add(disjunction);
            }

//         Фильтрация контента по контенту
            Junction filteredJunction = RestrictionsFactoryUtil.disjunction();
            if (displayTerms.getEnableArchiveNews()) {
                filteredJunction.add(PropertyFactoryUtil.forName(PROPERTY_STATUS).eq(WorkflowConstants.STATUS_EXPIRED));
            }
            filteredJunction.add(PropertyFactoryUtil.forName(PROPERTY_STATUS).eq(WorkflowConstants.STATUS_APPROVED));
            junctionJournalArticle.add(filteredJunction);
            dynamicQueryJournalArticle.add(junctionJournalArticle);
            journalArticles = JournalArticleDTOLocalServiceUtil.getDynamicQuery(dynamicQueryJournalArticle);
        }

//         Локализация контента
        LocalisationLocalServiceUtil.localize(journalArticles, locale);
        return journalArticles;
    }

    /**
     * Возвращает список resourcePrimKey сущностей JournalArticle по заданной категории
     *
     * @param categoriesName имя категории
     */
    private static List<Long> getJournalArticlesResourcePrimKeysByCategories(String categoriesName) {
        if (categoriesName == null) {
            throw new IllegalArgumentException("Can't get PrimKeys by null categories name.");
        }
        ClassLoader classLoader = PortalClassLoaderUtil.getClassLoader();
        DynamicQuery dynamicQueryAssetCategories = DynamicQueryFactoryUtil.forClass(AssetCategory.class, "assetCategories", classLoader);
        Junction junctionAssetCategories = RestrictionsFactoryUtil.disjunction();
        junctionAssetCategories.add(RestrictionsFactoryUtil.ilike(PROPERTY_NAME, categoriesName));
        dynamicQueryAssetCategories.add(junctionAssetCategories);
        List<AssetCategory> assetCategories = new ArrayList<>();
        List<Long> resourcePrimKeyList = new ArrayList<>();
        try {
            assetCategories = AssetCategoryLocalServiceUtil.dynamicQuery(dynamicQueryAssetCategories);
        } catch (SystemException e) {
            log.info("Can't get DynamicQuery from AssetCategoryLocalServiceUtil.");
            log.error(e);
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
                    log.info("Can't get List of AssetEntry by categoryId " + assetCategory.getCategoryId() + ".");
                    log.error(e);
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
    private static List<Long> getJournalArticlesResourcePrimKeysByTag(String tagName) {
        if (tagName == null) {
            throw new IllegalArgumentException("Can't get PrimKeys by null tag's name.");
        }
        ClassLoader classLoader = PortalClassLoaderUtil.getClassLoader();
        DynamicQuery dynamicQueryAssetTag = DynamicQueryFactoryUtil.forClass(AssetTag.class, "assetTag", classLoader);
        Junction junctionAssetTag = RestrictionsFactoryUtil.disjunction();
        junctionAssetTag.add(RestrictionsFactoryUtil.ilike(PROPERTY_NAME, tagName));
        dynamicQueryAssetTag.add(junctionAssetTag);
        List<AssetTag> assetTags = new ArrayList<>();
        List<Long> resourcePrimKeyList = new ArrayList<>();
        try {
            assetTags = AssetTagLocalServiceUtil.dynamicQuery(dynamicQueryAssetTag);
        } catch (SystemException e) {
            log.info("Can't get List of AssetTag from AssetTagLocalServiceUtil.");
            log.error(e);
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
                    log.info("Can't get List of AssetEntry by tagId " + assetTag.getTagId() + ".");
                    log.error(e);
                }
            }
        }
        return resourcePrimKeyList;
    }
}