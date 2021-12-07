/*
 * ThinkingRock, a project management tool for Personal Computers.
 * Copyright (C) 2006 Avente Pty Ltd
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package au.com.trgtd.tr.export.data.model;

import static au.com.trgtd.tr.appl.Constants.ID_DEFAULT_CONTEXT;
import static au.com.trgtd.tr.appl.Constants.ID_DEFAULT_TOPIC;
import java.util.ArrayList;
import static java.util.stream.Collectors.toList;
import java.util.List;
import java.util.stream.IntStream;
import tr.model.Data;
import tr.model.Item.Item;
import tr.model.criteria.Value;
import tr.model.project.Project;

public final class DataModel {

    private final Data data;
    private final DataModelHelper helper;

    public final List<ContactBean> contacts;
    public final List<ContextBean> contexts;
    public final List<EnergyBean> energies;
    public final List<PriorityBean> priorities;
    public final List<ReferenceBean> references;
    public final List<SomedayBean> somedays;
    public final List<ThoughtBean> thoughts;
    public final List<TopicBean> topics;
    public final List<ItemBean> items;
    public final List<ItemBean> futureItems;
    public final List<ItemBean> templateItems;

    /**
     * Construct a new instance.
     *
     * @param data The data.
     */
    public DataModel(Data data) {
        this.data = data;
        this.helper = new DataModelHelper();
        this.contacts = loadContacts();
        this.contexts = loadContexts();
        this.energies = loadEnergies();
        this.priorities = loadPriorities();
        this.references = loadReferences();
        this.somedays = loadSomedays();
        this.thoughts = loadThoughts();
        this.topics = loadTopics();
        this.items = loadItems(data.getRootActions(), data.getRootProjects());
        this.futureItems = loadItems(data.getRootFutures());
        this.templateItems = loadItems(data.getRootTemplates());
    }

    private List<ContactBean> loadContacts() {
        return data.getActorManager().list().stream()
                .map(a -> helper.convertContact(a))
                .collect(toList());
    }

    private List<ContextBean> loadContexts() {
        return data.getContextManager().list().stream()
                .filter(c -> (c.getID() != ID_DEFAULT_CONTEXT))
                .map(c -> helper.convertContext(c))
                .collect(toList());
    }

    private List<EnergyBean> loadEnergies() {
        List<Value> values = data.getEnergyCriterion().values.list();
        return IntStream.range(0, values.size())
                .mapToObj(i -> helper.convertEnergy(values.get(i), i))
                .collect(toList());
    }

    private List<PriorityBean> loadPriorities() {
        List<Value> values = data.getPriorityCriterion().values.list();
        return IntStream.range(0, values.size())
                .mapToObj(i -> helper.convertPriority(values.get(i), i))
                .collect(toList());
    }

    private List<ReferenceBean> loadReferences() {
        return data.getInformationManager().list().stream()
                .map(r -> helper.convertReference(r))
                .collect(toList());
    }

    private List<SomedayBean> loadSomedays() {
        return data.getFutureManager().list().stream()
                .map(s -> helper.convertSomeday(s))
                .collect(toList());
    }

    private List<ThoughtBean> loadThoughts() {
        return data.getThoughtManager().list().stream()
                .map(t -> helper.convertThought(t))
                .collect(toList());
    }

    private List<TopicBean> loadTopics() {
        return data.getTopicManager().list().stream()
                .filter(t -> (t.getID() != ID_DEFAULT_TOPIC))
                .map(t -> helper.convertTopic(t))
                .collect(toList());
    }

    private List<ItemBean> loadItems(Project... projects) {
        List<ItemBean> list = new ArrayList<>();
        for (Project project : projects) {
            loadItems(list, project);
        }
        return list;
    }

    private void loadItems(List<ItemBean> list, Project project) {

        list.addAll(loadItems(project));

        project.getChildren(Project.class)
                .stream()
                .forEach((subproject) -> {
                    loadItems(list, subproject);
                });
    }

    private List<ItemBean> loadItems(Project project) {        
        List<Item> list = project.getChildren();        
        return IntStream.range(0, list.size())
                .mapToObj(i -> helper.convertItem(list.get(i), i))
                .collect(toList());
    }

}
