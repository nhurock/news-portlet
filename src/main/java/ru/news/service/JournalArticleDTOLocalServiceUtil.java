package ru.news.service;

import com.liferay.portal.kernel.dao.orm.*;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
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
import com.liferay.portlet.journal.service.JournalArticleLocalServiceUtil;
import ru.news.comparator.JournalArticleDTOComparator;
import ru.news.mapper.JournalArticleMap;
import ru.news.model.JournalArticleDTO;
import ru.news.search.JournalArticleDTODisplayTerms;

import java.util.*;

public class JournalArticleDTOLocalServiceUtil {

    private static final String PROPERTY_TITLE = "title";
    private static final String PROPERTY_RESOURCE_PRIM_KEY = "resourcePrimKey";
    private static final String PROPERTY_CONTENT = "content";
    private static final String PROPERTY_NAME = "name";
    private static Log log = LogFactoryUtil.getLog(JournalArticleDTOLocalServiceUtil.class);

    /**
     * Удаяет архивные записи из коллекции
     *
     * @param journalArticleDTOS список новстей.
     */
    private static void deleteExpiredJournalArticles(List<JournalArticleDTO> journalArticleDTOS) {
        ListIterator<JournalArticleDTO> iterator = journalArticleDTOS.listIterator();
        while (iterator.hasNext()) {
            if (isExpired(iterator.next())) {
                iterator.remove();
            }
        }
    }

    /**
     * Возвращает список актуальных {@link JournalArticleDTO} cо статусом Approved и Expired
     */
    private static List<JournalArticleDTO> getApprovedAndExpiredJournalArticlesLatestVersion() {
        List<JournalArticleDTO> journalArticleDTOS = getLatestVersionApprovedAndExpiredJournalArticles();
        journalArticleDTOS.sort(new JournalArticleDTOComparator().reversed());
        return journalArticleDTOS;
    }

    /**
     * Возвращает последнюю версию WebContent {@link JournalArticleDTO}
     *
     * @param articleId ID {@link JournalArticle}
     * @param groupId   groupId {@link JournalArticle}
     */
    public static JournalArticleDTO getLatestVersion(long groupId, String articleId) {
        JournalArticle journalArticle = null;
        try {
            journalArticle = JournalArticleLocalServiceUtil.getLatestArticle(groupId, articleId);
        } catch (PortalException | SystemException e) {
            log.trace(e);
        }
        return JournalArticleMap.toDto(journalArticle);
    }

    /**
     * Возвращает новости из запроса {@link DynamicQuery}
     *
     * @param dynamicQuery для поиска {@link JournalArticle}
     */
    private static List<JournalArticleDTO> getDynamicQuery(DynamicQuery dynamicQuery) {
        List<JournalArticle> dynamicQuery1 = null;
        try {
            dynamicQuery1 = JournalArticleLocalServiceUtil.dynamicQuery(dynamicQuery);
        } catch (SystemException e) {
            log.trace(e);
        }

        if (dynamicQuery1 == null) return null;

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
     * Возвращает список актуальных {@link JournalArticle} со статусом Approved и Expired
     */
    private static List<JournalArticleDTO> getLatestVersionApprovedAndExpiredJournalArticles() {
        HashMap<String, JournalArticleDTO> journalArticleHashMap = new HashMap<>();
        try {
            for (JournalArticle journalArticle : JournalArticleLocalServiceUtil.getArticles()) {
                String articleId = journalArticle.getArticleId();
                long groupId = journalArticle.getGroupId();

                if (!journalArticle.isInTrash() && (journalArticle.isApproved() || journalArticle.isExpired()))
                    if (!journalArticleHashMap.containsKey(articleId)) {
                        journalArticleHashMap.put(articleId, getLatestVersion(groupId, articleId));
                    }
            }
        } catch (SystemException e) {
            log.trace(e);
        }
        return new ArrayList<>(journalArticleHashMap.values());
    }

    /**
     * Возращает true если запись имеент статус Expired
     */
    private static Boolean isExpired(JournalArticleDTO journalArticleDTO) {
        JournalArticle journalArticle = null;
        try {
            journalArticle = JournalArticleLocalServiceUtil.getLatestArticle(journalArticleDTO.getGroupId(), journalArticleDTO.getArticleId());
        } catch (PortalException | SystemException e) {
            log.trace(e);
        }
        return journalArticle != null && journalArticle.isExpired();
    }

    /**
     * Возвращает список новостей из поиска, список фиксированного размера
     *
     * @param displayTerms параметры запроса
     * @param start        номер первой записи
     * @param end          номер последней записи
     */
    public static List<JournalArticleDTO> getJournalArticles(JournalArticleDTODisplayTerms displayTerms, int start, int end) {
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

    /**
     * Возращает список новстей в соответсвии с формой запроса
     *
     * @param displayTerms параметры поиска
     */
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
                junctionJournalArticle = RestrictionsFactoryUtil.disjunction();
                junctionJournalArticle.add(PropertyFactoryUtil.forName(PROPERTY_TITLE).like("%" + displayTerms.getKeywords() + "%"));
                junctionJournalArticle.add(PropertyFactoryUtil.forName(PROPERTY_CONTENT).like("%" + displayTerms.getKeywords() + "%"));
            }
            dynamicQueryJournalArticle.add(junctionJournalArticle);
            journalArticles = JournalArticleDTOLocalServiceUtil.getDynamicQuery(dynamicQueryJournalArticle);
        }

//         Фильтрация контента по контенту
        if (!displayTerms.getEnableArchiveNews()) {
//          Удаление архивных новостей
            JournalArticleDTOLocalServiceUtil.deleteExpiredJournalArticles(journalArticles);
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
    private static List<Long> getJournalArticlesResourcePrimKeysByCategories(String categoriesName) {
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
            log.trace(e);
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
                    log.trace(e);
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
            log.trace(e);
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
                    log.trace(e);
                }
            }
        }
        return resourcePrimKeyList;
    }
}