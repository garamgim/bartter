import { useMutation, useSuspenseQuery } from '@tanstack/react-query';
import { createFileRoute } from '@tanstack/react-router';
import { useState } from 'react';
import { Map, MapMarker } from "react-kakao-maps-sdk"

import GeneralButton from '@/components/Buttons/GeneralButton';
import LocationContainer from '@/components/LocationContainer';
import barter from '@/services/barter';
import useRootStore from '@/store';
import { getPosition } from '@/util/geolocation.ts';
import querykeys from '@/util/querykeys';

import styles from './../changeLocation.module.scss'

export const Route = createFileRoute('/_layout/_protected/profile/changelocation/_layout/')({
  component: ProfileChangeLocation,
});

export default function ProfileChangeLocation() {

  const userId : UserId = useRootStore((state) => state.userId)
  // 변경된 위치 저장
  const [userPosition, setUserPosition] = useState<{ latitude: number; longitude: number } | null>(null);

  // 내위치 조회하려고 했는데 위치넘버와 동네만 나옴.
  const { data } = useSuspenseQuery({
    queryKey: [querykeys.LOCATION, userId],
    queryFn: ()=> barter.getUserLocation(userId)
  })
  const currentLocation = data.data.data.name.split(' ').slice(1,2) + ' ' + data.data.data.name.split(' ').slice(2,3);
  console.log(1111,currentLocation)

  // 위치 정보 변경
  const changeLocationMutation = useMutation({
    mutationFn: ({ longitude, latitude }: { longitude: number; latitude: number }) => {
      return barter.getCurrentLocation({ longitude, latitude });
    },
    onSuccess: () => {
      console.log('위치변경 성공!')
    },
  });

  async function handleLocation() {
    try {
      const {
        coords: { latitude, longitude },
      } = await getPosition();

      // 상태에 위치 데이터 저장
      setUserPosition({ latitude, longitude });

      // 여기에서 위치 변경 mutation을 사용할 수도 있습니다.
      changeLocationMutation.mutate({ longitude, latitude }) ;

      return { latitude, longitude };
    } catch (error) {
      console.error('위치 수정: 위치 가져오기 실패', error);
    }
  }

  return (
    <div>
      <LocationContainer currentLocation={currentLocation} />
      <div className={styles.map}>
      {userPosition ? (
        <Map 
        id="map"
        center={{
          lat: userPosition.latitude,
          lng: userPosition.longitude,
        }}
        style={{
          width: "100%",
          height: "350px",
        }}
        level={3}>
        <MapMarker position={{ lat: userPosition.latitude, lng: userPosition.longitude }}>
        수정된 위치
        </MapMarker>
        </Map>
      ) : (
        <p>위치 데이터를 불러오세요.</p>
      )}
      </div>
      <div className={styles.button}>
      <GeneralButton
        buttonStyle={{ size: 'large', style: 'primary' }}
        onClick={handleLocation}
      >
        위치 수정
      </GeneralButton>
      </div>
    </div>
  );
}