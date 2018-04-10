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
import ru.news.comparator.JournalArticleDTOComparator;
import ru.news.mapper.JournalArticleMap;
import ru.news.model.JournalArticleDTO;
import ru.news.search.JournalArticleDTODisplayTerms;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class JournalArticleDTOLocalServiceUtil {

    private static final String PROPERTY_TITLE = "title";
    private static final String PROPERTY_RESOURCE_PRIM_KEY = "resourcePrimKey";
    private static final String PROPERTY_CONTENT = "content";
    private static final String PROPERTY_NAME = "name";
    private static final String PROPERTY_STATUS = "status";
    private static final String PROPERTY_ARTICLE = "articleId";
    private static final String PROPERTY_VERSION = "version";
    private static final String PROPERTY_UUID = "uuid";
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
            log.error("Can't get JournalArticles last version by groupId " + groupId + " and articleId " + articleId + "." + e);
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
        List<JournalArticle> journalArticleList = null;
        try {
            journalArticleList = JournalArticleLocalServiceUtil.dynamicQuery(dynamicQuery);
        } catch (SystemException e) {
            log.error("Can't get DynamicQuery from JournalArticleLocalServiceUtil." + e);
        }

        if (journalArticleList == null) {
            throw new IllegalArgumentException("Can't work with null List<JournalArticle>.");
        }

       /* HashMap<String, JournalArticleDTO> journalArticleHashMap = new HashMap<>();
        for (JournalArticle journalArticle : journalArticleList) {
            String articleId = journalArticle.getArticleId();
            if (!journalArticleHashMap.containsKey(articleId)) {
                journalArticleHashMap.put(articleId, getLatestVersion(journalArticle.getGroupId(), journalArticle.getArticleId()));
            }
        }*/
        log.info("Get List<JournalArticleDTO> by DynamicQuery and there is " + journalArticleList.size() + " elements.");
        return JournalArticleMap.toDto(journalArticleList);
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
        Locale locale = displayTerms.getLocale();
        DynamicQuery dynamicQuery = getJournalArticleDynamicQuery(displayTerms);
        List<JournalArticleDTO> articleDTOS = getJournalArticleData(dynamicQuery, locale);
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
        DynamicQuery dynamicQuery = getJournalArticleDynamicQuery(displayTerms);
        if (dynamicQuery != null) {
            try {
                return (int) JournalArticleLocalServiceUtil.dynamicQueryCount(dynamicQuery);
            } catch (SystemException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    /**
     * Возвращает список новостей из запроса.
     *
     * @param dynamicQuery запрос
     * @param locale       язык пользователя
     */
    private static List<JournalArticleDTO> getJournalArticleData(DynamicQuery dynamicQuery, Locale locale) {
        List<JournalArticleDTO> journalArticles = getDynamicQuery(dynamicQuery);
        LocalisationLocalServiceUtil.localize(journalArticles, locale);
        journalArticles.sort(new JournalArticleDTOComparator().reversed());
        return journalArticles;
    }

    /**
     * Возращает DynamicQuery из запроса формы поиска
     *
     * @param displayTerms параметры поиска
     */
    private static DynamicQuery getJournalArticleDynamicQuery(JournalArticleDTODisplayTerms displayTerms) {
        if (displayTerms == null) {
            throw new IllegalArgumentException("Can't get List<JournalArticle> with null JournalArticleDTODisplayTerms.");
        }
        ClassLoader classLoader = PortalClassLoaderUtil.getClassLoader();
        DynamicQuery dynamicQueryJournalArticle = DynamicQueryFactoryUtil.forClass(JournalArticle.class, "journalArticle", classLoader);
        Junction junctionJournalArticle;
        String displayTermsKeywords = displayTerms.getKeywords();

        if (Validator.isBlank(displayTermsKeywords) && (!displayTerms.isAdvancedSearch())) {
//            Получения данных без фильтров поиска
            junctionJournalArticle = RestrictionsFactoryUtil.disjunction();
        } else {
//            Расширенный поиск
            if (displayTerms.isAdvancedSearch()) {
                log.info("Advanced search.");
                if (displayTerms.isAndOperator()) {
                    junctionJournalArticle = RestrictionsFactoryUtil.conjunction();
                } else {
                    junctionJournalArticle = RestrictionsFactoryUtil.disjunction();
                }

                String title = displayTerms.getTitle();
                if (!Validator.isBlank(title)) {
                    log.info("Search by title " + title);
                    junctionJournalArticle.add(RestrictionsFactoryUtil.ilike(PROPERTY_TITLE, "%" + title + "%"));
                }
                String tagName = displayTerms.getTag();
                if (!Validator.isBlank(tagName)) {
                    log.info("Search by tag " + tagName);
                    Junction disjunction = RestrictionsFactoryUtil.disjunction();
                    disjunction.add(PropertyFactoryUtil.forName(PROPERTY_RESOURCE_PRIM_KEY).in(getJournalArticlesResourcePrimKeysByTag(tagName)));

                    junctionJournalArticle.add(disjunction);
                }
                String categoryName = displayTerms.getCategory();
                if (!Validator.isBlank(categoryName)) {
                    log.info("Search by category " + displayTerms.getCategory());
                    Junction disjunction = RestrictionsFactoryUtil.disjunction();
                    disjunction.add(PropertyFactoryUtil.forName(PROPERTY_RESOURCE_PRIM_KEY).in(getJournalArticlesResourcePrimKeysByCategories(categoryName)));

                    junctionJournalArticle.add(disjunction);
                }

            } else {
                log.info("Simple search by keywords " + displayTermsKeywords);
//                 Поиск по основному полю
                junctionJournalArticle = RestrictionsFactoryUtil.conjunction();
                Junction disjunction = RestrictionsFactoryUtil.disjunction();
                disjunction.add(RestrictionsFactoryUtil.ilike(PROPERTY_TITLE, "%" + displayTermsKeywords + "%"));
                disjunction.add(RestrictionsFactoryUtil.ilike(PROPERTY_CONTENT, "%" + displayTermsKeywords + "%"));
                junctionJournalArticle.add(disjunction);
            }

        }
//         Фильтрация контента по контенту
        Junction filteredJunction = RestrictionsFactoryUtil.disjunction();
        if (displayTerms.getEnableArchiveNews()) {
            log.info("Enable archive news.");
            filteredJunction.add(PropertyFactoryUtil.forName(PROPERTY_STATUS).eq(WorkflowConstants.STATUS_EXPIRED));
        }

        filteredJunction.add(PropertyFactoryUtil.forName(PROPERTY_STATUS).eq(WorkflowConstants.STATUS_APPROVED));
        junctionJournalArticle.add(filteredJunction);
        dynamicQueryJournalArticle.add(junctionJournalArticle);

        // Получение актуальной версии JournalArticle
        DynamicQuery subSelect = DynamicQueryFactoryUtil.forClass(JournalArticle.class, "child", classLoader)
                .add(PropertyFactoryUtil.forName(PROPERTY_ARTICLE).eqProperty("parent.articleId")).setProjection(ProjectionFactoryUtil.max(PROPERTY_VERSION));

        DynamicQuery dynamicQueryUuidOfLastVersionJournalArticle = DynamicQueryFactoryUtil.forClass(JournalArticle.class, "parent", classLoader)
                .add(PropertyFactoryUtil.forName(PROPERTY_VERSION).in(subSelect))
                .setProjection(ProjectionFactoryUtil.property(PROPERTY_UUID));

        dynamicQueryJournalArticle.add(PropertyFactoryUtil.forName(PROPERTY_UUID).in(dynamicQueryUuidOfLastVersionJournalArticle));
        return dynamicQueryJournalArticle;
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
            log.error("Can't get DynamicQuery from AssetCategoryLocalServiceUtil. " + e);
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
                    log.error("Can't get List of AssetEntry by categoryId " + assetCategory.getCategoryId() + "." + e);
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
            log.error("Can't get List of AssetTag from AssetTagLocalServiceUtil. " + e);
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
                    log.error("Can't get List of AssetEntry by tagId " + assetTag.getTagId() + "." + e);
                }
            }
        }
        return resourcePrimKeyList;
    }
}

