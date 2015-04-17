Alfresco ECM Services for IBM Lotus Quickr are an example of a Content Service Mapper that provides content services on top of Alfresco and maps them to a service interface that Lotus Quickr expects in the Quickr application. The Content Service Mapper allows Quickr to navigate the Alfresco repository, store and retrieve content from Quickr into Alfresco, and manipulate metadata in the content objects from Quickr.

To provide the mapping to Quickr, IBM provided a guide of the services required to access an ECM system like Alfresco or IBM FileNet. This interface required implementing a combination of SOAP for content transfer and AtomPub services for metadata manipulation. See http;//www.ibm.com/developerworks/lotus/library/quickr-web-services/ for more details. IBM provides a WSDL for the SOAP interfaces and can generate the abstract implementations of the WSDL.

For SOAP interfaces, the Alfresco Content Mapper for Quickr used the same web services infrastructure as the Alfresco CMIS web services. The AtomPub interface used web scripts implemented in Java for performance reasons. Both implementations used the Alfresco Java Foundation API.

The following example shows one portion of the implementation that lists children of a document as a feed. Notice that the Mapper is primarily handling the translation of terminology and the mechanics of accessing the objects. The concepts are roughly the same between Quickr and Alfresco. A collection is an artifact of AtomPub rather than Quickr, but the notions of folders, content, and children are the same in both systems.

```
publicclass AlfrescoAtomBasedFeedServiceImpl implements AtomBasedFeedService
{    

  private NodeService nodeService;

  private PersonService personService;

  public Feed getListDocuments(String id)
  {
    NodeRef storeRef = newNodeRef(id);

    // <feed>
    Feed feed = newFOMFeed();

    feed.setBaseUri("/library/" + id + "/");

    // <generator>
    feed.setGenerator("", "1.0", "Teamspace Documents");

    // <id>
    feed.setId("urn:lsid:ibm.com:td:" + id);

    // <link>
    feed.addLink("feed", "self");
    feed.addLink("http://quickr.acme.com/wps/mypoc?uri=dm:" + id
             + "&verb=view", "alternate");
    feed.addLink("feed?pagesize=2&page=3", "next");
    feed.addLink("feed?pagesize=2&page=1", "previous");

    String contentName = (String) nodeService.getProperty(storeRef,
               ContentModel.PROP_NAME);

    // <collection>
    feed.setCollection(new FOMCollection(contentName, "feed",
               new String[] { "*/*" }));

    String authorName = (String) nodeService.getProperty(storeRef,
                            ContentModel.PROP_AUTHOR);
    String email = (String) nodeService.getProperty(
                              personService.getPerson(authorName),
                              ContentModel.PROP_EMAIL);
    String userName = (String) nodeService.getProperty(
                              personService.getPerson(authorName),
                              ContentModel.PROP_USERNAME);

    // feed.addAuthor(createPerson(storeRef));

    // <author>
    feed.addAuthor(authorName, email, "uid=" + userName + ",o=acme");

    // <title>
    feed.setTitle(contentName);

    // <updated>
    feed.setUpdated((Date) nodeService.getProperty(storeRef,
                                    ContentModel.PROP_MODIFIED));

    // add<entry>
    for (ChildAssociationRef childAssoc : nodeService.getChildAssocs(storeRef))
    {
       NodeRef childRef = childAssoc.getChildRef();
       String childName = (String) nodeService.getProperty(childRef,
                                         ContentModel.PROP_NAME);

       Entry entry = newFOMEntry();

       // <id>
       entry.setId("urn:lsid:ibm.com:td:" + childRef.getId());

       // <link>
       entry.addLink("document/" + childRef.getId() + "/entry", "self");
       entry.addLink("http://quickr.acme.com/wps/mypoc?uri=dm:" + childRef.getId()
         + "&verb=view", "alternate");
       entry.addLink("document/" + childRef.getId() + "/entry", "edit");
       if (nodeService.getProperty(childRef, ContentModel.PROP_CONTENT) != null)
       {
         entry.addLink("document/" + childRef.getId() + "/entry", "edit-media");
         entry.addLink("document/" + childRef.getId() + "/entry", "enclosure",
               (String) nodeService.getProperty(childRef,
         ContentModel.PROP_CONTENT), childName, "en",
               (Long) nodeService.getProperty(childAssoc.getChildRef(),
         ContentModel.PROP_SIZE_CURRENT));

         // <category>
         entry.addCategory("tag:ibm.com,2006:td/type", "document", "document");
       }
       else
       {
         // <category>
         entry.addCategory("tag:ibm.com,2006:td/type", "folder", "folder");
       }

       authorName = (String) nodeService.getProperty(childRef,
         ContentModel.PROP_AUTHOR);
       email = (String) nodeService.getProperty(personService.getPerson(authorName),
         ContentModel.PROP_EMAIL);
       userName = (String) nodeService.getProperty(
         personService.getPerson(authorName),
         ContentModel.PROP_USERNAME);

       // <author>
       entry.addAuthor(authorName, email, "uid=" + userName + ",o=acme");

       // <title>
       entry.setTitle(childName);

       // <published>
       entry.setPublished((Date) nodeService.getProperty(childRef,
         ContentModel.PROP_CREATED));

       // <updated>
       entry.setUpdated((Date) nodeService.getProperty(childRef,
         ContentModel.PROP_MODIFIED));

       return feed;
    }
```

This example implements a small but important portion of the Quickr API as a Content Mapper Service. This is the service to get a list of documents that may be used in a portlet or document browser. The service, implemented as an Atom-based feed, references a space node by ID and returns the metadata associated with the space. It then iterates through the children of the node through the child associations. If the child is a document, it provides a link to the content. If it is a folder, it provides a link to the space representing that folder. This is likely to be a common pattern in many Content Mappers.