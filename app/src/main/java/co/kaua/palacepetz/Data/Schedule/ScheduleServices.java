package co.kaua.palacepetz.Data.Schedule;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.POST;

public interface ScheduleServices {

    @POST("user/create/schedule")
    Call<DtoSchedule> CreateSchedule(@Body DtoSchedule dtoSchedule);

    @DELETE("user/schedule/cancel")
    Call<DtoSchedule> CancelSchedule(@Body DtoSchedule dtoSchedule);
}
