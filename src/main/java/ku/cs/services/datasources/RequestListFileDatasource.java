package ku.cs.services.datasources;

import ku.cs.models.request.Request;
import ku.cs.models.request.RequestList;
import ku.cs.services.RequestService;
import ku.cs.services.SelectedDayService;

public class RequestListFileDatasource implements Datasource<RequestList> {
    private  final JsonListFileDatasource<Request, RequestList> delegate;
    private final RequestService requestService = new RequestService();
    public RequestListFileDatasource(String directoryName, String fileName) {
        this.delegate = new JsonListFileDatasource<>(
                directoryName,
                fileName,
                RequestList::new,
                RequestList::getRequestList,
                RequestList::addRequest,
                Request.class
        );
    }


    @Override
    public RequestList readData() {
        return delegate.readData();
    }

    @Override
    public void writeData(RequestList data) {
        delegate.writeData(data);
    }
}
